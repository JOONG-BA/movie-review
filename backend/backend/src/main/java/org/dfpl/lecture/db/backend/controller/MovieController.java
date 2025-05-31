package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSearchResultDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /* 인기 영화 페이지 수동 페치 (관리용) */
    @PostMapping("/fetch")
    public ResponseEntity<List<MovieDB>> fetchAndSave(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(movieService.fetchAndSavePopularMovies(page));
    }

    /* 페이지 범위 페치 */
    @PostMapping("/fetch-range")
    public ResponseEntity<List<MovieDB>> fetchRange(
            @RequestParam(defaultValue = "1") int from,
            @RequestParam(defaultValue = "2") int to
    ) {
        return ResponseEntity.ok(movieService.fetchAndSavePopularMovies(from, to));
    }

    /* 검색 */
    @GetMapping("/search")
    public ResponseEntity<List<MovieSearchResultDTO>> search(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(movieService.searchMovies(query));
    }

    /* 상세 (러닝타임·감독·배우·갤러리·예고편 포함) */
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailDTO> detail(
            @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", defaultValue = "ko-KR") Locale locale
    ) {
        return ResponseEntity.ok(movieService.getDetail(id, locale));
    }
}
