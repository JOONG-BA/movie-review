package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PopularController: 이미 DB에 저장된 영화 중
 * “장르별/한국 개봉/인기도 Top N” 조회 전용
 */
@RestController
@RequestMapping("/api/popular")
@RequiredArgsConstructor
public class PopularController {

    private final MovieService movieService;

    // GET /api/popular/{genreId}?limit={개수}
    @GetMapping("/{genreId}")
    public ResponseEntity<List<MovieSummaryDTO>> getTopNPopularInKoreaByGenre(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "10") int limit) {

        List<MovieSummaryDTO> list = movieService.getTopNPopularInKoreaByGenre(genreId, limit);
        return ResponseEntity.ok(list);
    }
}
