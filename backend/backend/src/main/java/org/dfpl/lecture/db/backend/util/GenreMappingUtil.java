package org.dfpl.lecture.db.backend.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GenreMappingUtil {
    /** TMDB 장르 ID → 한글 이름 매핑 */
    public static final Map<Integer, String> GENRE_ID_TO_NAME;
    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(28,   "액션");
        map.put(12,   "모험");
        map.put(16,   "애니메이션");
        map.put(35,   "코미디");
        map.put(80,   "범죄");
        map.put(99,   "다큐멘터리");
        map.put(18,   "드라마");
        map.put(10751,"가족");
        map.put(14,   "판타지");
        map.put(36,   "역사");
        map.put(27,   "공포");
        map.put(10402,"음악");
        map.put(9648, "미스터리");
        map.put(10749,"로맨스");
        map.put(878,  "SF");
        map.put(10770,"TV 영화");
        map.put(53,   "스릴러");
        map.put(10752,"전쟁");
        map.put(37,   "서부");
        GENRE_ID_TO_NAME = Collections.unmodifiableMap(map);
    }

    private GenreMappingUtil() { }
}
