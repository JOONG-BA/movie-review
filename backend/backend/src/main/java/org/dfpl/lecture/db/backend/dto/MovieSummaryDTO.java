package org.dfpl.lecture.db.backend.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MovieSummaryDTO {
    private Long tmdbId;
    private String title;
    private String posterPath;
    private Double popularity;
    private List<String> genres;
}
