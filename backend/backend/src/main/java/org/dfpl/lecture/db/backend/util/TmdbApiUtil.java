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
}
