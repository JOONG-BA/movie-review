package org.dfpl.lecture.db.backend.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDB {

    @Id
    private Long id;  // TMDB의 movie ID

    @Column(nullable = false)
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Lob
    private String overview;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String country;

    private Integer runtime;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "backdrop_path")
    private String backdropPath;

    /**
     * Movie ↔ Genre 는 N:M 관계입니다.
     * movie_genre 라는 조인 테이블을 사용하여 매핑합니다.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
