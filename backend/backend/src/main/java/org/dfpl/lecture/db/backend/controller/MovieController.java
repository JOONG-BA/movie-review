package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/popular")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * (1) 한국 개봉된 영화 중 인기도 TOP N 가져오기
     *    예: GET /api/popular/in-korea?limit=10
     */
    @GetMapping("/in-korea")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularInKorea(
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> list = movieService.getTopNPopularInKorea(limit);
        return ResponseEntity.ok(list);
    }

    /**
     * (2) 특정 장르 ID에 속하며 한국에서 개봉된 영화 중 인기도 TOP N 가져오기
     *    예: GET /api/popular/genre/28?limit=5  (28은 액션 장르 ID 예시)
     */
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularInKoreaByGenre(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> list = movieService.getTopNPopularInKoreaByGenre(genreId, limit);
        return ResponseEntity.ok(list);
    }
    /**
     * (검색 API) TMDb 검색 → DB에 없으면 상세 저장 → 최종적으로 DB 기반 Summary 반환
     *
     * 예) GET /api/movies/search?query=어벤져스&limit=10
     */
    @GetMapping("/search")
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<MovieSummaryDTO> results = movieService.searchAndSaveIfMissing(query, limit);
        return ResponseEntity.ok(results);
    }
}
