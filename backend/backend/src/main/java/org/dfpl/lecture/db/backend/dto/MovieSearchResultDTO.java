package org.dfpl.lecture.db.backend.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class MovieSearchResultDTO {

    private Long   tmdbId;
    private String title;
    private String posterPath;
    private String releaseDate;
    private Double popularity;

    public static MovieSearchResultDTO fromEntity(org.dfpl.lecture.db.backend.entity.MovieDB e) {
        return MovieSearchResultDTO.builder()
                .tmdbId(e.getTmdbId())
                .title(e.getTitle())
                .posterPath(e.getPosterPath())
                .releaseDate(e.getReleaseDate())
                .popularity(e.getPopularity())
                .build();
    }
}
