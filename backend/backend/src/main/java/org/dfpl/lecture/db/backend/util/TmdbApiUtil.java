package org.dfpl.lecture.db.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TmdbApiUtil {

    @Value("${tmdb.api.key}")
    private String apiKey;

    public String getPopularMoviesUrl(int page) {
        return "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey + "&language=ko-KR&page=" + page;
    }

    public String getCreditsUrl(long movieId) {
        return "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + apiKey + "&language=ko-KR";
    }

    public String getMovieDetailWithCreditsUrl(long movieId) {
        return "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey
                + "&language=ko-KR&append_to_response=credits";
    }

    public String getSearchMovieUrl(String query) {
        return "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&language=ko-KR&query=" + query;
    }
}
