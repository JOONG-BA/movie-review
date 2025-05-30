package org.dfpl.lecture.db.backend.service;

import org.dfpl.lecture.db.backend.dto.MovieSearchResultDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieService {

    @Autowired
    private TmdbApiUtil tmdbApiUtil;

    @Autowired
    private MovieRepository movieRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public MovieDetailDTO getMovieDetail(Long id) {
        return movieRepository.findById(id)
                .map(m -> MovieDetailDTO.builder()
                        .id(m.getId())
                        .title(m.getTitle())
                        .overview(m.getOverview())
                        .releaseDate(m.getReleaseDate())
                        .posterPath("https://image.tmdb.org/t/p/w500" + m.getPosterPath()) // 전체 URL 반환
                        .voteAverage(m.getVoteAverage())
                        .genres(m.getGenres())
                        .productionCountries(m.getProductionCountries())
                        .cast(m.getCast())
                        .crew(m.getCrew())
                        .category(m.getCategory())
                        .build())
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화를 찾을 수 없습니다."));
    }


    public List<MovieDB> fetchAndSavePopularMovies(int page) {
        String url = tmdbApiUtil.getPopularMoviesUrl(page);
        JsonNode resultList = restTemplate.getForObject(url, JsonNode.class).path("results");

        List<MovieDB> saved = new ArrayList<>();
        for (JsonNode movieSummary : resultList) {
            long movieId = movieSummary.path("id").asLong();

            if (movieRepository.existsById(movieId)) {
                System.out.println("⚠️ 이미 존재하는 영화, 건너뜀: id=" + movieId);
                continue;
            }

            try {
                MovieDB fullMovie = fetchFullMovieDetail(movieId);
                if (fullMovie != null) saved.add(fullMovie);
            } catch (Exception e) {
                System.err.println("❌ 상세정보 로딩 실패 (id=" + movieId + "): " + e.getMessage());
            }

            try {
                Thread.sleep(250); // API 호출 제한 고려
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        return movieRepository.saveAll(saved);
    }



    public List<MovieDB> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieDB fetchFullMovieDetail(long movieId) {
        String url = tmdbApiUtil.getMovieDetailWithCreditsUrl(movieId);
        JsonNode movie = restTemplate.getForObject(url, JsonNode.class);

        String genres = StreamSupport.stream(movie.path("genres").spliterator(), false)
                .map(n -> n.path("name").asText())
                .collect(Collectors.joining(", "));

        String countries = StreamSupport.stream(movie.path("production_countries").spliterator(), false)
                .map(n -> n.path("name").asText())
                .collect(Collectors.joining(", "));

        String cast = StreamSupport.stream(movie.path("credits").path("cast").spliterator(), false)
                .limit(5)
                .map(n -> n.path("name").asText())
                .collect(Collectors.joining(", "));

        String crew = StreamSupport.stream(movie.path("credits").path("crew").spliterator(), false)
                .filter(n -> n.path("job").asText().equals("Director"))
                .map(n -> n.path("name").asText())
                .distinct()
                .collect(Collectors.joining(", "));

        return MovieDB.builder()
                .id(movieId)
                .title(movie.path("title").asText())
                .overview(movie.path("overview").asText())
                .releaseDate(movie.path("release_date").asText())
                .posterPath(movie.path("poster_path").asText())
                .voteAverage(movie.path("vote_average").asDouble())
                .genres(genres)
                .productionCountries(countries)
                .cast(cast)
                .crew(crew)
                .category("movie")
                .build();
    }
    public List<MovieDB> fetchAndSavePopularMoviesRange(int startPage, int endPage) {
        List<MovieDB> all = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            try {
                List<MovieDB> pageResult = fetchAndSavePopularMovies(i);
                all.addAll(pageResult);
                System.out.println("✅ 저장 완료: page " + i);
                Thread.sleep(250); // TMDB 제한 고려
            } catch (Exception e) {
                System.err.println("❌ 에러 발생 (page " + i + "): " + e.getMessage());
            }
        }
        return all;
    }


    public List<MovieSearchResultDTO> searchMoviesByTitle(String query) {
        List<MovieSearchResultDTO> results = new ArrayList<>();
        List<MovieDB> fromDb = movieRepository.findByTitleContainingIgnoreCase(query);
        for (MovieDB m : fromDb) {
            results.add(MovieSearchResultDTO.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .releaseDate(m.getReleaseDate())
                    .posterPath("https://image.tmdb.org/t/p/w500" + m.getPosterPath())
                    .productionCountries(m.getProductionCountries())
                    .category(m.getCategory())
                    .build());
        }

        String url = tmdbApiUtil.getSearchMovieUrl(query);
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        JsonNode resultsNode = response.path("results");

        for (JsonNode node : resultsNode) {
            Long id = node.path("id").asLong();
            if (fromDb.stream().anyMatch(m -> m.getId().equals(id))) continue;

            results.add(MovieSearchResultDTO.builder()
                    .id(id)
                    .title(node.path("title").asText())
                    .releaseDate(node.path("release_date").asText())
                    .posterPath("https://image.tmdb.org/t/p/w500" + node.path("poster_path").asText())
                    .productionCountries(node.path("original_language").asText())
                    .category("movie")
                    .build());
        }

        return results;
    }
}
