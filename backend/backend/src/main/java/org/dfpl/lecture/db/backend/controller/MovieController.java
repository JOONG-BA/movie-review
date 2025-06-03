package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.SearchResultDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.data.domain.Page;
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
    /**
     * DB에 저장된 모든 영화를 voteCount 내림차순(인기순)으로 페이징 조회합니다.
     * GET /api/movies/popular?page={page}&size={size}
     *
     * @param page  페이지 번호 (0부터 시작; 기본값 0)
     * @param size  페이지 크기 (기본값 10)
     */
    @GetMapping("/popular")
    public ResponseEntity<Page<MovieDB>> getAllPopular(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            Page<MovieDB> result = movieService.findAllPopular(page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/popular/api")
    public ResponseEntity<List<SearchResultDTO>> getPopularFromApi(
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        try {
            List<SearchResultDTO> results = movieService.getPopularFromApi(page);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    /**
     * TMDB API를 이용해 특정 장르(genreId) 기준으로 인기 영화 목록을 가져옵니다.
     * GET /api/movies/popular/api/genre?genreId={genreId}&page={page}
     *
     * @param genreId TMDB 장르 ID (예: 28=액션, 35=코미디 등)
     * @param page    페이지 번호 (1부터 시작; 기본값 1)
     */
    @GetMapping("/popular/api/genre")
    public ResponseEntity<List<SearchResultDTO>> getPopularByGenreFromApi(
            @RequestParam("genreId") Long genreId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        try {
            List<SearchResultDTO> results = movieService.getPopularByGenreFromApi(genreId, page);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException iae) {
            // genreId가 없거나 잘못된 경우 400 Bad Request
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
