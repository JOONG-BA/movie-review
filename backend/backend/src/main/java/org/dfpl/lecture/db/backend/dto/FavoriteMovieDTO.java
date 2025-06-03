package org.dfpl.lecture.db.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteMovieDTO {
    private Long movieId;
    private String title;
    private String posterPath;
}
