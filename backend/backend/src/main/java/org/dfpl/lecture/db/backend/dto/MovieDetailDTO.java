package org.dfpl.lecture.db.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieDetailDTO {
    private Long id;

    // 기본 정보
    private String title;
    private String originalTitle;
    private String overview;
    private String releaseYear;      // “2025-06-03” 형태의 date에서 앞 4자리만 사용
    private String country;
    private Integer runtime;        // 런타임(분 단위)

    private List<String> genres;     // 장르 이름 리스트

    // TMDB 제공 평점
    private Double voteAverage;      // TMDB 10점 만점
    private Integer voteCount;       // TMDB 투표 수

    // 우리의 리뷰 통합 평점을 추가할 수도 있음(현재 단계에선 제외)
    // private Double ourRatingAverage;
    // private Integer ourRatingCount;

    //poster backdrop

    private String backdrop_path;
    private String poster_path;

    // 크루 & 캐스트
    private PersonDTO director;      // 감독 한 명
    private List<CastDTO> cast;      // 배우 리스트

    // 갤러리
    private List<ImageDTO> galleryImages;

    // 예고편 / 트레일러
    private List<VideoDTO> trailers;

    public void setPosterUrl(String posterImageUrl) {
        this.poster_path = posterImageUrl;
    }

    public void setBackdropUrl(String backdropImageUrl) {
        this.backdrop_path = backdropImageUrl;
    }
}
