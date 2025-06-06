package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_db_hard")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MovieDB {
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Lob
    private String overview;

    private Double popularity;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "backdrop_url")
    private String backdropUrl;

    // 4개의 장르 ID 칼럼을 추가
    @Column(name = "genre1")
    private Long genre1;

    @Column(name = "genre2")
    private Long genre2;

    @Column(name = "genre3")
    private Long genre3;

    @Column(name = "genre4")
    private Long genre4;
}

