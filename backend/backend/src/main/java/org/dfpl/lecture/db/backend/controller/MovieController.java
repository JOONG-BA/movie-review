package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.dto.SearchResultDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<List<MovieSummaryDTO>> searchMovies(
            @RequestParam("query") String query,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        try {
            List<MovieSummaryDTO> results = movieService.searchMovies(query, page);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            // 간단히 500 상태로 에러 메시지를 리턴합니다.
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }
    /**
     * DB에 저장된 모든 영화를 voteCount 내림차순(인기순)으로 페이징 조회합니다.
     * GET /api/movies/popular?page={page}&size={size}
     *
     * @param page  페이지 번호 (0부터 시작; 기본값 0)
     */
    @GetMapping("/popular/api")
    public ResponseEntity<List<MovieSummaryDTO>> getPopular(
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        try {
            List<MovieSummaryDTO> list = movieService.getPopularMovies(page);
            return ResponseEntity.ok(list);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * TMDB API를 이용해 특정 장르(genreId) 기준으로 인기 영화 목록을 가져옵니다.
     * GET /api/movies/popular/api/genre?genreId={genreId}&page={page}
     *
     * @param genreId TMDB 장르 ID (예: 28=액션, 35=코미디 등)
     * @param page    페이지 번호 (1부터 시작; 기본값 1)
     */
    @GetMapping("/popular/genre/api")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularByGenre(
            @RequestParam("genre") int genreId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        try {
            List<MovieSummaryDTO> list = movieService.getPopularByGenre(genreId, page);
            return ResponseEntity.ok(list);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
