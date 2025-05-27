package org.dfpl.lecture.db.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailDTO {
    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private Double voteAverage;
    private String genres;
    private String productionCountries;
    private String cast;
    private String crew;
    private String category;
}
