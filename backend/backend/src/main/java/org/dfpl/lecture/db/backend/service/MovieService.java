package org.dfpl.lecture.db.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.*;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmdbApiUtil tmdb;
    private final Environment env;   // 모든 이미지 base-url 접근용

    /* ------------------------------------------------------------------ */
    /* 내부 헬퍼: TMDb path → 완전 URL                                     */
    /* ------------------------------------------------------------------ */
    private String img(String path, String sizeKey) {
        if (path == null || path.isBlank()) return null;
        String key = "tmdb.image.base-url." + (sizeKey == null ? "w500" : sizeKey);
        String base = env.getProperty(key, "https://image.tmdb.org/t/p/w500");
        return base + path;
    }

    /* ------------------------------------------------------------------ */
    /* 검색 (DB → TMDb 폴백)                                              */
    /* ------------------------------------------------------------------ */
    @Transactional(readOnly = true)
    public List<MovieSearchResultDTO> searchMovies(String keyword) {

        String norm = keyword.replaceAll("\\s+", "")
                .toLowerCase(Locale.ROOT);

        // 1) 캐시(DB) 우선
        List<MovieDB> cached = movieRepository.searchByRelevance(norm);
        if (!cached.isEmpty()) {
            return cached.stream()
                    .map(MovieSearchResultDTO::fromEntity)
                    .peek(dto -> dto.setPosterPath(img(dto.getPosterPath(), "w342")))
                    .toList();
        }

        // 2) TMDb 폴백
        List<MovieSearchResultDTO> fromTmdb = tmdb.searchMovies(keyword)
                .stream()
                .peek(dto -> dto.setPosterPath(img(dto.getPosterPath(), "w342")))
                .toList();
        saveIfAbsent(fromTmdb);
        return fromTmdb;
    }

    /* ------------------------------------------------------------------ */
    /* 인기 영화 페이지 저장                                              */
    /* ------------------------------------------------------------------ */
    @Transactional
    public List<MovieDB> fetchAndSavePopularMovies(int page) {
        List<MovieSearchResultDTO> pageList = tmdb.fetchPopularMovies(page);
        return pageList.stream()
                .filter(dto -> !movieRepository.existsByTmdbId(dto.getTmdbId()))
                .map(MovieDB::fromSearchDTO)
                .map(movieRepository::save)
                .toList();
    }

    @Transactional
    public List<MovieDB> fetchAndSavePopularMovies(int fromPage, int toPage) {
        List<MovieDB> all = new ArrayList<>();
        for (int p = fromPage; p <= toPage; p++) {
            all.addAll(fetchAndSavePopularMovies(p));
        }
        return all;
    }

    /* ------------------------------------------------------------------ */
    /* 상세 정보                                                           */
    /* ------------------------------------------------------------------ */
    @Transactional(readOnly = true)
    public MovieDetailDTO getDetail(Long movieId, Locale locale) {

        MovieDB base = movieRepository.findById(movieId).orElse(null);
        JsonNode bundle = tmdb.fetchMovieBundle(movieId, locale);

        /* 장르 */
        List<String> genres = new ArrayList<>();
        bundle.path("genres").forEach(g -> genres.add(g.path("name").asText()));

        /* CAST 10명 */
        List<CastDTO> casts = StreamSupport.stream(bundle.path("credits").path("cast").spliterator(), false)
                .limit(10)
                .map(c -> CastDTO.builder()
                        .id(c.path("id").asLong())
                        .name(c.path("name").asText())
                        .role(c.path("character").asText())
                        .profilePath(img(c.path("profile_path").asText(null), "w185"))
                        .build())
                .toList();

        /* 감독 */
        List<CastDTO> directors = StreamSupport.stream(bundle.path("credits").path("crew").spliterator(), false)
                .filter(crew -> "Director".equals(crew.path("job").asText()))
                .map(d -> CastDTO.builder()
                        .id(d.path("id").asLong())
                        .name(d.path("name").asText())
                        .role("Director")
                        .profilePath(img(d.path("profile_path").asText(null), "w185"))
                        .build())
                .toList();

        /* 갤러리 10장 (원본 사이즈) */
        List<ImageDTO> gallery = StreamSupport.stream(bundle.path("images").path("backdrops").spliterator(), false)
                .limit(10)
                .map(imgNode -> ImageDTO.builder()
                        .filePath(img(imgNode.path("file_path").asText(), "original"))
                        .width(imgNode.path("width").asInt())
                        .height(imgNode.path("height").asInt())
                        .build())
                .toList();

        /* 예고편 */
        List<VideoDTO> videos = StreamSupport.stream(bundle.path("videos").path("results").spliterator(), false)
                .filter(v -> "Trailer".equals(v.path("type").asText()))
                .map(v -> VideoDTO.builder()
                        .key(v.path("key").asText())
                        .site(v.path("site").asText())
                        .name(v.path("name").asText())
                        .type(v.path("type").asText())
                        .build())
                .toList();

        return MovieDetailDTO.builder()
                .id(movieId)
                .title(Optional.ofNullable(base).map(MovieDB::getTitle)
                        .orElse(bundle.path("title").asText()))
                .overview(bundle.path("overview").asText())
                .releaseDate(bundle.path("release_date").asText())
                .runtime(bundle.hasNonNull("runtime") ? bundle.path("runtime").asInt() : null)
                .genres(genres)
                .posterPath(img(bundle.path("poster_path").asText(null), "w500"))
                .backdropPath(img(bundle.path("backdrop_path").asText(null), "w780"))
                .casts(casts)
                .directors(directors)
                .gallery(gallery)
                .videos(videos)
                .build();
    }

    /* ------------------------------------------------------------------ */
    /* 내부 util                                                           */
    /* ------------------------------------------------------------------ */
    private void saveIfAbsent(List<MovieSearchResultDTO> dtoList) {
        dtoList.stream()
                .filter(dto -> !movieRepository.existsByTmdbId(dto.getTmdbId()))
                .map(MovieDB::fromSearchDTO)
                .forEach(movieRepository::save);
    }
}
