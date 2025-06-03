package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.SearchResultDTO;
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
     * /api/movies/detail/{id} 호출 시 TMDB에서 세부정보를 받아와 JSON으로 반환
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable Long id) throws Exception {
        MovieDetailDTO detail = movieService.getMovieDetail(id);
        return ResponseEntity.ok(detail);
    }
    /**
     * 1) TMDB 영화 검색 API (/search/movie) 연동
     * GET /api/movies/search?query={키워드}&page={페이지번호}
     *
     * @param query 검색어 (필수)
     * @param page  TMDB 검색 결과 페이지 (옵션; 기본 1)
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchResultDTO>> searchMovies(
            @RequestParam("query") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        try {
            List<SearchResultDTO> results = movieService.searchMovies(query, page);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            // 간단히 500 상태로 에러 메시지를 리턴합니다.
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }
}
