package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.dto.SearchResultDTO;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.service.FavoriteService;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final FavoriteService favoriteService;

    // ─────────────────────────────────────────────────────────────────────
    // 1) DB 검색 (제목 or 개요 LIKE) → 페이징 DTO 반환
    //    GET /api/movies/db/search?keyword={키워드}&page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<Page<SearchResultDTO>> searchInDb(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<SearchResultDTO> dtoPage = movieService.searchMoviesInDb(query, page, size);
        return ResponseEntity.ok(dtoPage);
    }

    // ─────────────────────────────────────────────────────────────────────
    // 2) DB에 저장된 전체 영화 → 인기순 페이징 DTO 반환
    //    GET /api/movies/db/popular?page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/popular/api")
    public ResponseEntity<Page<SearchResultDTO>> getPopularFromDb(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<SearchResultDTO> dtoPage = movieService.getPopularFromDb(page, size);
        return ResponseEntity.ok(dtoPage);
    }

    // ─────────────────────────────────────────────────────────────────────
    // 3) DB에서 장르별 인기순 페이징 DTO 반환
    //    GET /api/movies/db/popular/genre/{genreId}?page={페이지}&size={크기}
    // ─────────────────────────────────────────────────────────────────────
    @GetMapping("/popular/genre/api")
    public ResponseEntity<Page<SearchResultDTO>> getPopularByGenreFromDb(
            @RequestParam("genre") Long genreId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Page<SearchResultDTO> dtoPage = movieService.getPopularByGenreFromDb(genreId, page, size);
        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(
            @PathVariable Long id,
            Authentication auth    // ← Authentication으로 변경
    ) throws Exception {
        MovieDetailDTO detail = movieService.getMovieDetail(id);

        // 익명(비로그인)이 아니면 즐겨찾기 여부 체크
        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {

            String email = auth.getName();
            boolean fav  = favoriteService.isFavorite(email, id);
            detail.setFavorite(fav);

        } else {
            detail.setFavorite(false);
        }

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
