package org.dfpl.lecture.db.backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TmdbApiUtil {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate rest;
    private final ObjectMapper om = new ObjectMapper();

    /* ---------- URL 빌더 ---------- */

    /** locale(ko-KR 등) 기반 영화 상세 + credits·images·videos */
    public URI buildMovieDetailUri(long movieId, Locale locale) {
        return UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/movie/{id}")
                .queryParam("api_key", apiKey)
                .queryParam("language", locale)
                .queryParam("append_to_response", "credits,images,videos")
                .queryParam("include_image_language", locale + ",null")   // 로컬 + 언어 없음
                .build(movieId);
    }

    /* ---------- 파싱 헬퍼 ---------- */

    public JsonNode callGet(URI uri) {
        RequestEntity<Void> req = new RequestEntity<>(HttpMethod.GET, uri);
        String json = rest.exchange(req, String.class).getBody();
        try {
            return om.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("TMDb 응답 파싱 실패: " + e.getMessage(), e);
        }
    }

    /** 상세 + 부가 리소스를 한 번에 가져온다 */
    public JsonNode fetchMovieBundle(long movieId, Locale locale) {
        return callGet(buildMovieDetailUri(movieId, locale));
    }
}
