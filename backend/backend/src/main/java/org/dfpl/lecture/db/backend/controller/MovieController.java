package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * (1) 영화 상세조회 API
     *     GET /api/movies/{tmdbId}
     */
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable Long tmdbId) {
        MovieDetailDTO detail = movieService.getMovieDetailByTmdbId(tmdbId);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    /**
     * (2) 검색 API
     *     GET /api/movies/search?query={키워드}&limit={개수}
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> results = movieService.searchAndSaveIfMissing(query, limit);
        return ResponseEntity.ok(results);
    }

    /**
     * (3) 한국 개봉된 영화 중 인기도 TOP N
     *     GET /api/movies/in-korea?limit={개수}
     */
    @GetMapping("/in-korea")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularInKorea(
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> list = movieService.getTopNPopularInKorea(limit);
        return ResponseEntity.ok(list);
    }

    /**
     * (4) 특정 장르 ID에 속하며, 한국 개봉된 영화 중 인기도 TOP N
     *     GET /api/movies/genre/{genreId}?limit={개수}
     */
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularInKoreaByGenre(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> list = movieService.getTopNPopularInKoreaByGenre(genreId, limit);
        return ResponseEntity.ok(list);
    }
}
