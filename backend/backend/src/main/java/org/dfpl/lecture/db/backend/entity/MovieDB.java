package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie_db", indexes = {
        @Index(name = "idx_tmdb_id", columnList = "tmdb_id", unique = true)
})
public class MovieDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----- TMDb 고유 식별자 -----
    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Long tmdbId;

    // ----- 영화 기본 정보 -----
    private String title;                // 한국어 제목 (가능하면)
    @Column(columnDefinition = "TEXT")
    private String overview;             // 한국어 줄거리 (가능하면)
    private String originalTitle;        // 원제목(영문 등)
    private String originalLanguage;     // 원제목 언어 코드 (ex: "en")

    private LocalDate releaseDate;       // TMDb release_date
    private Integer runtime;             // 상영 시간(분)

    private Double popularity;           // TMDb 인기도 점수
    private Double voteAverage;          // TMDb 평균 평점
    private Integer voteCount;           // 평점 개수

    private String status;               // ex) "Released", "Post Production"
    private String tagline;              // 짧은 태그라인

    private Long budget;                 // 제작비
    private Long revenue;                // 수익

    private String homepage;             // 공식 홈페이지 URL

    // ----- 이미지/포스터 경로 -----
    private String backdropPath;         // 배경 이미지 경로 ("/xxxx.jpg")
    private String posterPath;           // 포스터 이미지 경로 ("/yyyy.jpg")

    // ----- 한국 개봉 정보 -----
    private Boolean releasedInKorea;     // 한국에서 개봉했는지 여부
    private LocalDate releaseDateKorea;  // 한국 개봉일자

    // ----- 관계: 영화 ↔ 장르 (다대다) -----
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieGenre> movieGenres = new HashSet<>();

    // ----- 관계: 영화 ↔ 배우(캐스트) (다대다를 풀어서 일대다+다대일) -----
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovieCast> movieCasts = new HashSet<>();

    /** 편의 생성자 (필요히 수정 가능) */
    public MovieDB(Long tmdbId, String title, String overview,
                   String originalTitle, String originalLanguage,
                   LocalDate releaseDate, Integer runtime,
                   Double popularity, Double voteAverage, Integer voteCount,
                   String status, String tagline, Long budget, Long revenue,
                   String homepage, String backdropPath, String posterPath,
                   Boolean releasedInKorea, LocalDate releaseDateKorea) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.overview = overview;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.status = status;
        this.tagline = tagline;
        this.budget = budget;
        this.revenue = revenue;
        this.homepage = homepage;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.releasedInKorea = releasedInKorea;
        this.releaseDateKorea = releaseDateKorea;
    }

    /** 연관관계 편의 메서드: Genre 추가 */
    public void addGenre(Genre genre) {
        MovieGenre mg = new MovieGenre(this, genre);
        movieGenres.add(mg);
        genre.getMovieGenres().add(mg);
    }

    /** 연관관계 편의 메서드: Cast 추가 */
    public void addCast(Cast cast, String characterName) {
        MovieCast mc = new MovieCast(this, cast, characterName);
        movieCasts.add(mc);
        cast.getMovieCasts().add(mc);
    }
}
