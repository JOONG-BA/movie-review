package org.dfpl.lecture.db.backend.controller;

import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/boxoffice")
    public String getBoxOffice(@RequestParam String date) {
        return movieService.getBoxOfficeData(date);
    }

    @GetMapping("/details")
    public String getMovieDetails(@RequestParam String movieCode) {
        return movieService.getMovieDetail(movieCode);
    }

    // TMDb 영화 상세정보 조회
    @GetMapping("/tmdb/details")
    public String getTmdbDetails(@RequestParam String movieId) {
        return movieService.getTmdbMovieDetail(movieId);
    }

    // TMDb 영화 검색
    @GetMapping("/tmdb/search")
    public String searchTmdb(@RequestParam String query) {
        return movieService.searchTmdbMovies(query);
    }
}
