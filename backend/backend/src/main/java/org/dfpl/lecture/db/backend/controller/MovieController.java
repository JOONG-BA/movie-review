package org.dfpl.lecture.db.backend.controller;

import org.dfpl.lecture.db.backend.DTO.MovieSearchResultDTO;
import org.dfpl.lecture.db.backend.DTO.MovieDetailDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // 단일 페이지 가져오기
    @PostMapping("/fetch")
    public List<MovieDB> fetchAndSave(@RequestParam(defaultValue = "1") int page) {
        return movieService.fetchAndSavePopularMovies(page);
    }

    // 범위 페이지 가져오기
    @PostMapping("/fetch-range")
    public List<MovieDB> fetchRange(
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "10") int end) {
        return movieService.fetchAndSavePopularMoviesRange(start, end);
    }

    // 전체 목록 반환
    @GetMapping
    public List<MovieDB> getAll() {
        return movieService.getAllMovies();
    }

    // 상세 조회
    @GetMapping("/{id}")
    public MovieDetailDTO getMovieDetail(@PathVariable Long id) {
        return movieService.getMovieDetail(id);
    }

    // 검색
    @GetMapping("/search")
    public List<MovieSearchResultDTO> searchMovies(@RequestParam String query) {
        return movieService.searchMoviesByTitle(query);
    }
}
