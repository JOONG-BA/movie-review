package org.dfpl.lecture.db.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MovieDetailDTO {
    private Long id;

    private String title;
    private String originalTitle;
    private String overview;
    private String releaseYear;
    private String country;
    private Integer runtime;
    private List<String> genres;

    private Double voteAverage;     // TMDB 기준 (10점 만점)
    private Integer voteCount;

    private PersonDTO director;
    private List<CastDTO> cast;

    private List<ImageDTO> galleryImages;
    private List<VideoDTO> trailers;
}
