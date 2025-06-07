package org.dfpl.lecture.db.backend.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TmdbApiUtil {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String CREDITS_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w1280";
    // TODO: 직접 하드코딩하기보다는 application.properties나 환경변수로 옮겨 두는 것을 권장합니다.
    private static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMjE3YmI3MzZiN2E5NzU1MzAyZGY4MzVkNTM3NzI0ZCIsIm5iZiI6MTc0ODAxODY3Mi44NzY5OTk5LCJzdWIiOiI2ODMwYTVmMGI4NTAwYzkwODVlYjI1NDYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.GFi6vG9vKZkGbfF00fv0RAyzIcdILnP-EzfcAOeiEnM";
    private static final String LANGUAGE = "ko-KR";
    private static final String REGION = "KR";

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 주어진 endpoint(쿼리 포함 되었다고 가정)를
     * TMDB API_BASE_URL + endpoint 로 GET 요청 객체를 만들어 반환합니다.
     */
    public static Request buildRequest(String endpointWithQuery) {
        return new Request.Builder()
                .url(API_BASE_URL + endpointWithQuery)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", BEARER_TOKEN)
                .build();
    }

    /** OkHttpClient 인스턴스를 반환합니다. */
    public static OkHttpClient getClient() {
        return client;
    }

    /**
     * TMDB에서 주는 파일 경로(file_path)만으로는 이미지가 표시되지 않기 때문에,
     * IMAGE_BASE_URL + filePath 형태로 전체 URL을 구성합니다.
     */
    public static String getImageUrl(String filePath) {
        return (filePath != null && !filePath.isEmpty()) ? IMAGE_BASE_URL + filePath : null;
    }

    public static String getPosterImageUrl(String filePath) {
        return (filePath != null && !filePath.isEmpty()) ? POSTER_BASE_URL + filePath : null;
    }

    public static String getBackdropImageUrl(String filePath) {
        return (filePath != null && !filePath.isEmpty()) ? BACKDROP_BASE_URL + filePath : null;
    }
    public static String getCreditsImageUrl(String filePath) {
        return (filePath != null && !filePath.isEmpty()) ? CREDITS_BASE_URL + filePath : null;
    }

    /**
     * endpoint 문자열 뒤에 언어 파라미터 `?language=ko-KR` 또는 `&language=ko-KR`를 붙여 줍니다.
     */
    public static String withLanguage(String path) {
        return path.contains("?")
                ? path + "&language=" + LANGUAGE
                : path + "?language=" + LANGUAGE;
    }

    /**
     * endpoint 문자열 뒤에 언어 + 지역 파라미터(`?language=ko-KR&region=KR` 또는 `&language=ko-KR&region=KR`)를 붙여 줍니다.
     */
    public static String withLanguageAndRegion(String path) {
        return path.contains("?")
                ? path + "&language=" + LANGUAGE + "&region=" + REGION
                : path + "?language=" + LANGUAGE + "&region=" + REGION;
    }

}
