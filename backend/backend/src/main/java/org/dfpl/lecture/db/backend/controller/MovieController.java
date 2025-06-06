package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // ─────────────────────────────────────────────────────────────────────
    // 1) DB 검색 (제목 or 개요 LIKE) → 페이징 DTO 반환
    //    GET /api/movies/db/search?keyword={키워드}&page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<Page<MovieSummaryDTO>> searchInDb(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<MovieSummaryDTO> dtoPage = movieService.searchMoviesInDb(keyword, page, size);
        return ResponseEntity.ok(dtoPage);
    }

    // ─────────────────────────────────────────────────────────────────────
    // 2) DB에 저장된 전체 영화 → 인기순 페이징 DTO 반환
    //    GET /api/movies/db/popular?page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/popular")
    public ResponseEntity<Page<MovieSummaryDTO>> getPopularFromDb(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<MovieSummaryDTO> dtoPage = movieService.getPopularFromDb(page, size);
        return ResponseEntity.ok(dtoPage);
    }

    // ─────────────────────────────────────────────────────────────────────
    // 3) DB에서 장르별 인기순 페이징 DTO 반환
    //    GET /api/movies/db/popular/genre/{genreId}?page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/popular/genre/{genreId}")
    public ResponseEntity<Page<MovieSummaryDTO>> getPopularByGenreFromDb(
            @PathVariable Long genreId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<MovieSummaryDTO> dtoPage = movieService.getPopularByGenreFromDb(genreId, page, size);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable Long id) throws Exception {
        MovieDetailDTO detail = movieService.getMovieDetail(id);
        return ResponseEntity.ok(detail);
    }


    @PostMapping("/load-all")
    public ResponseEntity<Void> loadAllMovies() {
        try {
            movieService.loadAllAvailable();
            return ResponseEntity.ok().build();
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(502).build();
        }
    }
}
