package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "movies_list")
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

    @Column(name = "popularity")
    private Double popularity;

    @Column(name = "release_date")
    private String releaseDate;  // MovieSummaryDTO의 String 형식(예: "2021-10-01")

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "poster_url")
    private String posterUrl;    // MovieSummaryDTO의 full URL

    @Column(name = "backdrop_url")
    private String backdropUrl;  // MovieSummaryDTO의 full URL

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
