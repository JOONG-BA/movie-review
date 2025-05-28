package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movie_db") // ← 꼭 명시
public class MovieDB {


    @Id
    private Long id;

    private String title;

    @Column(length = 1000)
    private String overview;

    private String releaseDate;
    private String posterPath;
    private Double voteAverage;

    @Column(length = 1000)
    private String genres;

    @Column(length = 1000)
    private String productionCountries;

    private String category; // movie, tv 등

    @Column(length = 1000)
    private String cast;

    @Column(length = 1000)
    private String crew;
}
