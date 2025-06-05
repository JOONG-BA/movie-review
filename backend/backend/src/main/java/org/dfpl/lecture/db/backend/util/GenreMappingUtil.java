package org.dfpl.lecture.db.backend.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GenreMappingUtil {
    /** TMDB 장르 ID → 한글 이름 매핑 */
    public static final Map<Long, String> GENRE_ID_TO_NAME;
    static {
        Map<Long, String> map = new HashMap<>();
        map.put(28L,   "액션");
        map.put(12L,   "모험");
        map.put(16L,   "애니메이션");
        map.put(35L,   "코미디");
        map.put(80L,   "범죄");
        map.put(99L,   "다큐멘터리");
        map.put(18L,   "드라마");
        map.put(10751L,"가족");
        map.put(14L,   "판타지");
        map.put(36L,   "역사");
        map.put(27L,   "공포");
        map.put(10402L,"음악");
        map.put(9648L, "미스터리");
        map.put(10749L,"로맨스");
        map.put(878L,  "SF");
        map.put(10770L,"TV 영화");
        map.put(53L,   "스릴러");
        map.put(10752L,"전쟁");
        map.put(37L,   "서부");
        GENRE_ID_TO_NAME = Collections.unmodifiableMap(map);
    }

    private GenreMappingUtil() { }
}
