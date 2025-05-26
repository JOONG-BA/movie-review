package org.dfpl.lecture.db.backend.service;

import org.dfpl.lecture.db.backend.DTO.TmdbMovieResponse;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private TmdbApiUtil tmdbApiUtil;

    @Autowired
    private MovieRepository movieRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<MovieDB> fetchAndSavePopularMovies(int page) {
        String url = tmdbApiUtil.getPopularMoviesUrl(page);
        TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

        List<MovieDB> movies = response.getResults().stream().map(result ->
                MovieDB.builder()
                        .id(result.getId())
                        .title(result.getTitle())
                        .overview(result.getOverview())
                        .releaseDate(result.getRelease_date())
                        .posterPath(result.getPoster_path())
                        .voteAverage(result.getVote_average())
                        .build()
        ).collect(Collectors.toList());

        return movieRepository.saveAll(movies);
    }

    public List<MovieDB> getAllMovies() {
        return movieRepository.findAll();
    }
}
