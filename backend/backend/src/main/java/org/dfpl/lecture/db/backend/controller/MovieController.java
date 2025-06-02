package org.dfpl.lecture.db.backend.controller;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // (1) 인기 영화 한 페이지 가져와서 DB에 저장
    //    POST /api/movies/fetch?page={page}
    @PostMapping("/fetch")
    public ResponseEntity<List<MovieDB>> fetchAndSave(
            @RequestParam(defaultValue = "1") int page) {
        List<MovieDB> saved = movieService.fetchAndSavePopularMovies(page);
        return ResponseEntity.ok(saved);
    }

    // (2) 인기 영화 여러 페이지(from~to) 가져와서 DB에 저장
    //    POST /api/movies/fetch-range?from={from}&to={to}
    @PostMapping("/fetch-range")
    public ResponseEntity<List<MovieDB>> fetchRange(
            @RequestParam(defaultValue = "1") int from,
            @RequestParam(defaultValue = "2") int to) {
        List<MovieDB> saved = movieService.fetchAndSavePopularMovies(from, to);
        return ResponseEntity.ok(saved);
    }

    // (3) TMDb 실시간 검색 + DB에 없으면 저장
    //    GET /api/movies/search?query={검색어}
    @GetMapping("/search")
    public ResponseEntity<List<MovieSummaryDTO>> search(
            @RequestParam String query) {
        List<MovieSummaryDTO> results = movieService.searchAndSaveIfMissing(query, 10);
        return ResponseEntity.ok(results);
    }

    // (4) 개별 영화 상세 (러닝타임·감독·배우·이미지·비디오 포함)
    //    GET /api/movies/{tmdbId}
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDetailDTO> detail(
            @PathVariable Long tmdbId) {
        MovieDetailDTO detail = movieService.getDetail(tmdbId);
        return ResponseEntity.ok(detail);
    }

    // (5) 특정 장르 ID에 속하며, 한국 개봉된 영화 중 인기도 TOP N
    //    GET /api/movies/genre/{genreId}?limit={개수}
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<MovieSummaryDTO>> getPopularInKoreaByGenre(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieSummaryDTO> list = movieService.getTopNPopularInKoreaByGenre(genreId, limit);
        return ResponseEntity.ok(list);
    }
}
