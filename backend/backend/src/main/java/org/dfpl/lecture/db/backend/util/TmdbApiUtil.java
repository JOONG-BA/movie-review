package org.dfpl.lecture.db.backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.CastDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.GenreDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class TmdbApiUtil {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.bearer}")
    private String bearerToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 1) TMDb 인기 영화 목록 (page 단위) → ID 리스트 반환
     */
    public List<Long> fetchPopularMovieIds(int page) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/movie/popular")
                .queryParam("language", "ko-KR")
                .queryParam("page", page)
                .build()
                .toUri();

        JsonNode root = callGet(uri);
        return StreamSupport.stream(root.path("results").spliterator(), false)
                .map(r -> r.path("id").asLong())
                .toList();
    }

    /**
     * 2) TMDb 영화 상세정보 + 크레딧 + release_dates + 이미지 + 비디오 호출
     *    → language=ko-KR, append_to_response=credits,release_dates,images,videos
     */
    public MovieDetailDTO fetchMovieDetail(Long movieId) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/movie/" + movieId)
                .queryParam("language", "ko-KR")
                .queryParam("append_to_response", "credits,release_dates,images,videos")
                .build()
                .toUri();

        JsonNode root = callGet(uri);

        // -------------------------------
        // 1) 기본 영화 정보 파싱
        // -------------------------------
        Long id = root.path("id").asLong();
        String title = root.path("title").asText(null);
        String overview = root.path("overview").asText(null);
        String originalTitle = root.path("original_title").asText(null);
        String originalLanguage = root.path("original_language").asText(null);

        LocalDate releaseDate = null;
        if (!root.path("release_date").asText("").isBlank()) {
            releaseDate = LocalDate.parse(root.path("release_date").asText(), DateTimeFormatter.ISO_DATE);
        }

        Integer runtime = root.path("runtime").isInt() ? root.path("runtime").asInt() : null;
        Double popularity = root.path("popularity").asDouble(0.0);
        Double voteAverage = root.path("vote_average").asDouble(0.0);
        Integer voteCount = root.path("vote_count").asInt(0);
        String status = root.path("status").asText(null);
        String tagline = root.path("tagline").asText(null);
        Long budget = root.path("budget").asLong(0L);
        Long revenue = root.path("revenue").asLong(0L);
        String homepage = root.path("homepage").asText(null);
        String backdropPath = root.path("backdrop_path").asText(null);
        String posterPath = root.path("poster_path").asText(null);

        // -------------------------------
        // 2) 장르(Genre) 파싱
        // -------------------------------
        List<GenreDTO> genreList = StreamSupport.stream(root.path("genres").spliterator(), false)
                .map(g -> new GenreDTO(
                        g.path("id").asLong(),
                        g.path("name").asText(null)
                ))
                .toList();

        // -------------------------------
        // 3) 크레딧(Credits → Cast) 파싱
        // -------------------------------
        List<CastDTO> castList = StreamSupport.stream(root.path("credits").path("cast").spliterator(), false)
                .map(c -> new CastDTO(
                        c.path("id").asLong(),
                        c.path("name").asText(null),
                        c.path("character").asText(null),
                        c.path("profile_path").asText(null)
                ))
                .toList();

        // -------------------------------
        // 4) Release Dates 파싱 (한국 개봉 여부/날짜)
        // -------------------------------
        Boolean releasedInKorea = false;
        LocalDate releaseDateKorea = null;
        JsonNode resultsNode = root.path("release_dates").path("results");
        for (JsonNode countryNode : resultsNode) {
            if ("KR".equalsIgnoreCase(countryNode.path("iso_3166_1").asText(null))) {
                JsonNode innerDates = countryNode.path("release_dates");
                String earliest = null;
                for (JsonNode rd : innerDates) {
                    String rdStr = rd.path("release_date").asText(null);
                    if (rdStr != null && rdStr.length() >= 10) {
                        String onlyDate = rdStr.substring(0, 10);
                        if (earliest == null || onlyDate.compareTo(earliest) < 0) {
                            earliest = onlyDate;
                        }
                    }
                }
                if (earliest != null) {
                    releasedInKorea = true;
                    releaseDateKorea = LocalDate.parse(earliest, DateTimeFormatter.ISO_DATE);
                }
                break;
            }
        }

        // -------------------------------
        // 5) 이미지 / 비디오 유무 체크 (옵션)
        // -------------------------------
        boolean hasImages = root.path("images").path("posters").size() > 0;
        boolean hasVideos = root.path("videos").path("results").size() > 0;

        // -------------------------------
        // 6) MovieDetailDTO 반환
        // -------------------------------
        return MovieDetailDTO.builder()
                .tmdbId(id)
                .title(title)
                .overview(overview)
                .originalTitle(originalTitle)
                .originalLanguage(originalLanguage)
                .releaseDate(releaseDate)
                .runtime(runtime)
                .popularity(popularity)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .status(status)
                .tagline(tagline)
                .budget(budget)
                .revenue(revenue)
                .homepage(homepage)
                .backdropPath(backdropPath)
                .posterPath(posterPath)
                .releasedInKorea(releasedInKorea)
                .releaseDateKorea(releaseDateKorea)
                .genres(genreList)
                .casts(castList)
                .hasImages(hasImages)
                .hasVideos(hasVideos)
                .build();
    }

    /** TMDb API GET 호출을 수행하는 공통 private 메서드 */
    private JsonNode callGet(URI uri) throws Exception {
        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Accept", "application/json")
                .build();

        var response = restTemplate.exchange(request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("TMDb API 호출 에러: " + response.getStatusCode());
        }
        return objectMapper.readTree(response.getBody());
    }
}
