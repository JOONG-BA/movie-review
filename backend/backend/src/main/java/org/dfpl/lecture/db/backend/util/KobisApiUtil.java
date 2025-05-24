package org.dfpl.lecture.db.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KobisApiUtil {

    @Value("${kobis.api.key}")  // application.properties 에서 키를 읽음
    private String apiKey;

    // 특정 날짜 박스오피스 리스트 조회 URL 생성
    public String getMovieListUrl(String targetDt) {
        return "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json"
                + "?key=" + apiKey
                + "&targetDt=" + targetDt;
    }
}
