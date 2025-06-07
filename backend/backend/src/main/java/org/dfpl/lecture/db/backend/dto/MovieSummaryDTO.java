package org.dfpl.lecture.db.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieSummaryDTO {
    private Long id;
    private String title;
    private String originalTitle;
    private String overview;
    private Double popularity;
    private String releaseDate;
    private Double voteAverage;
    private Integer voteCount;
    private String posterUrl;
    private String backdropUrl;

    /** 장르 목록: [{"id": 28, "name": "액션"}, {"id": 878, "name": "SF"}, ...] */
    private List<GenreDTO> genres;
}
