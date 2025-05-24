package org.dfpl.lecture.db.backend.service;

import org.dfpl.lecture.db.backend.util.KobisApiUtil;
import org.dfpl.lecture.db.backend.util.KmdbApiUtil;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    @Autowired
    private KobisApiUtil kobisApiUtil;

    @Autowired
    private KmdbApiUtil kmdbApiUtil;

    @Autowired
    private TmdbApiUtil tmdbApiUtil;

    private final RestTemplate restTemplate = new RestTemplate();

    // KOBIS 박스오피스 데이터 가져오기
    public String getBoxOfficeData(String date) {
        String url = kobisApiUtil.getMovieListUrl(date);
        return restTemplate.getForObject(url, String.class);
    }

    // KMDb 영화 상세정보 가져오기
    public String getMovieDetail(String movieCode) {
        String url = kmdbApiUtil.getMovieDetailUrl(movieCode);
        return restTemplate.getForObject(url, String.class);
    }

    // TMDb 영화 상세정보 가져오기
    public String getTmdbMovieDetail(String movieId) {
        String url = tmdbApiUtil.getMovieDetailUrl(movieId);
        return restTemplate.getForObject(url, String.class);
    }

    // TMDb 영화 검색 (예: 제목으로)
    public String searchTmdbMovies(String query) {
        String url = tmdbApiUtil.getMovieSearchUrl(query);
        return restTemplate.getForObject(url, String.class);
    }
}
