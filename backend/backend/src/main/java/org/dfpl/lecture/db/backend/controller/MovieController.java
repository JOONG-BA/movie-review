package org.dfpl.lecture.db.backend.controller;

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

    @PostMapping("/fetch")
    public List<MovieDB> fetchAndSave(@RequestParam(defaultValue = "1") int page) {
        return movieService.fetchAndSavePopularMovies(page);
    }

    @GetMapping
    public List<MovieDB> getAll() {
        return movieService.getAllMovies();
    }
}
