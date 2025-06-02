// src/main/java/org/dfpl/lecture/db/backend/controller/MovieController.java

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
     * (검색 API) TMDb 검색 → DB에 없으면 상세 저장 → 최종적으로 DB 기반 Summary 반환
     * 예: GET /api/movies/search?query=어벤져스&limit=10
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> results = movieService.searchAndSaveIfMissing(query, limit);
        return ResponseEntity.ok(results);
    }

    /**
     * (영화 상세조회 API) DB에 저장된 상세정보 반환
     * 예: GET /api/movies/{tmdbId}
     */
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable Long tmdbId) {
        MovieDetailDTO detail = movieService.getMovieDetailByTmdbId(tmdbId);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }
}
