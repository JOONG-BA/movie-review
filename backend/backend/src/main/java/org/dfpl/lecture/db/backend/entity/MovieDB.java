package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDB {
    @Id
    private Long id;

    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private Double voteAverage;
}
