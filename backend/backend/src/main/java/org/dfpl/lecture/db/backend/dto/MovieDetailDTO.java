package org.dfpl.lecture.db.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetailDTO {

    private Long tmdbId;
    private String title;
    private String overview;
    private String originalTitle;
    private String originalLanguage;
    private LocalDate releaseDate;
    private Integer runtime;
    private Double popularity;
    private Double voteAverage;
    private Integer voteCount;
    private String status;
    private String tagline;
    private Long budget;
    private Long revenue;
    private String homepage;
    private String backdropPath;
    private String posterPath;
    private Boolean releasedInKorea;
    private LocalDate releaseDateKorea;
    private List<GenreDTO> genres;
    private List<CastDTO> casts;
    private Boolean hasImages;   // 이미지 존재 여부
    private Boolean hasVideos;   // 비디오(예고편) 존재 여부

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenreDTO {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CastDTO {
        private Long id;
        private String name;
        private String characterName;
        private String profilePath;
    }
}
