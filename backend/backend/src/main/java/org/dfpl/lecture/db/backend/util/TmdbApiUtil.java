package org.dfpl.lecture.db.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TmdbApiUtil {

    @Value("${tmdb.api.key}")
    private String apiKey;

    // 영화 상세정보 가져오는 URL 예시 (movieId는 TMDb 고유 ID)
    public String getMovieDetailUrl(String movieId) {
        return "https://api.themoviedb.org/3/movie/" + movieId
                + "?api_key=" + apiKey
                + "&language=ko-KR";  // 한국어 응답 원할 시
    }

    // 영화 검색 API URL 예시 (query: 검색어)
    public String getMovieSearchUrl(String query) {
        return "https://api.themoviedb.org/3/search/movie"
                + "?api_key=" + apiKey
                + "&language=ko-KR"
                + "&query=" + query;
    }
}
