package org.dfpl.lecture.db.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KmdbApiUtil {

    @Value("${kmdb.api.key}")
    private String apiKey;

    public String getMovieDetailUrl(String movieCode) {
        return "https://api.kmdb.or.kr/path/to/detail?key=" + apiKey + "&movieCode=" + movieCode; // 05/24 api.kmdb.or.kr~~ 실제 필요한 주소로 수정 필요
    }
}
