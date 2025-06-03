package org.dfpl.lecture.db.backend.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;
import org.dfpl.lecture.db.backend.dto.*;
import org.dfpl.lecture.db.backend.entity.Genre;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.GenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final Gson gson = new Gson();

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    /**
     * TMDB에서 영화 세부정보를 호출하여 MovieDetailDTO를 만든 뒤,
     * 로컬 DB(MovieDB)에도 저장하는 예제 메서드입니다.
     */
    @Transactional
    public MovieDetailDTO fetchAndSaveMovie(Long movieId) throws IOException {
        // 1) TMDB에서 기본 영화 정보 JSON 가져오기
        JsonObject movieObj = fetchJson(TmdbApiUtil.withLanguage("/movie/" + movieId));

        // 2) MovieDetailDTO 조립
        MovieDetailDTO dto = new MovieDetailDTO();
        dto.setId(movieId);
        dto.setTitle(movieObj.get("title").getAsString());
        dto.setOriginalTitle(movieObj.get("original_title").getAsString());
        dto.setOverview(movieObj.get("overview").getAsString());

        String releaseDate = movieObj.get("release_date").getAsString();
        dto.setReleaseYear(releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : releaseDate);

        JsonArray countries = movieObj.getAsJsonArray("production_countries");
        dto.setCountry((countries != null && countries.size() > 0)
                ? countries.get(0).getAsJsonObject().get("name").getAsString()
                : null);

        dto.setRuntime(movieObj.get("runtime").isJsonNull() ? null : movieObj.get("runtime").getAsInt());
        dto.setVoteAverage(movieObj.get("vote_average").getAsDouble());
        dto.setVoteCount(movieObj.get("vote_count").getAsInt());

        JsonArray genresArr = movieObj.getAsJsonArray("genres");
        List<String> genreNames = new ArrayList<>();
        for (JsonElement e : genresArr) {
            genreNames.add(e.getAsJsonObject().get("name").getAsString());
        }
        dto.setGenres(genreNames);

        // (출연진, 이미지, 비디오 파싱은 생략하고, 앞서 작성된 예제대로 넣으면 됩니다.)
        // dto.setCast(...);
        // dto.setDirector(...);
        // dto.setGalleryImages(...);
        // dto.setTrailers(...);

        // 3) MovieDB 엔티티 생성 및 저장 (장르 매핑 포함)
        MovieDB entity = MovieDB.builder()
                .id(movieId)
                .title(dto.getTitle())
                .originalTitle(dto.getOriginalTitle())
                .overview(dto.getOverview())
                // releaseDate를 LocalDate로 변환 (releaseYear만 있으면 임의로 1월 1일 설정)
                .releaseDate(LocalDate.parse(dto.getReleaseYear() + "-01-01"))
                .country(dto.getCountry())
                .runtime(dto.getRuntime())
                .voteAverage(dto.getVoteAverage())
                .voteCount(dto.getVoteCount())
                .posterPath(extractFirstPosterPath(movieId))
                .backdropPath(extractFirstBackdropPath(movieId))
                .build();

        // 3-1) TMDB JSON의 genres 배열을 순회하며, Genre 엔티티를 DB에서 찾아서 리스트로 준비
        List<Genre> genreEntities = new ArrayList<>();
        for (JsonElement ge : genresArr) {
            JsonObject gObj = ge.getAsJsonObject();
            Long genreId = gObj.get("id").getAsLong();
            String genreName = gObj.get("name").getAsString();

            // DB에 이미 있으면 가져오고, 없으면 새로 저장
            Genre genre = genreRepository.findById(genreId)
                    .orElseGet(() -> {
                        Genre newGenre = Genre.builder()
                                .id(genreId)
                                .name(genreName)
                                .build();
                        return genreRepository.save(newGenre);
                    });
            genreEntities.add(genre);
        }
        // 3-2) MovieDB 엔티티에 장르 리스트 세팅 후 저장
        entity.setGenres(genreEntities);
        movieRepository.save(entity);

        return dto;
    }

    /**
     * /movie/{id}/images 호출 후, posters 배열 중 첫 번째 요소의 file_path 반환
     * (포스터 경로 없으면 null 반환)
     */
    private String extractFirstPosterPath(Long movieId) throws IOException {
        JsonObject imagesObj = fetchJson("/movie/" + movieId + "/images");
        JsonArray posters = imagesObj.getAsJsonArray("posters");
        if (posters != null && posters.size() > 0) {
            return posters.get(0).getAsJsonObject().get("file_path").getAsString();
        }
        return null;
    }

    /**
     * /movie/{id}/images 호출 후, backdrops 배열 중 첫 번째 요소의 file_path 반환
     * (백드롭 경로 없으면 null 반환)
     */
    private String extractFirstBackdropPath(Long movieId) throws IOException {
        JsonObject imagesObj = fetchJson("/movie/" + movieId + "/images");
        JsonArray backdrops = imagesObj.getAsJsonArray("backdrops");
        if (backdrops != null && backdrops.size() > 0) {
            return backdrops.get(0).getAsJsonObject().get("file_path").getAsString();
        }
        return null;
    }

    /**
     * 간단히 endpointWithQuery를 받아 요청을 보내고
     * JSON 본문을 JsonObject로 파싱하여 반환하는 헬퍼 메서드입니다.
     */
    private JsonObject fetchJson(String endpointWithQuery) throws IOException {
        Request request = TmdbApiUtil.buildRequest(endpointWithQuery);
        try (Response response = TmdbApiUtil.getClient().newCall(request).execute()) {
            String body = response.body().string();
            return JsonParser.parseString(body).getAsJsonObject();
        }
    }

    /**
     * 장르별 인기순 조회 예시:
     * PageRequest.of(page, size, Sort.by("voteCount").descending()) 같이 Pageable로 넘겨주면 됩니다.
     */
    public Page<MovieDB> findPopularByGenre(Long genreId, int page, int size) {
        return movieRepository.findByGenres_IdOrderByVoteCountDesc(
                genreId, PageRequest.of(page, size, Sort.by("voteCount").descending())
        );
    }
}
