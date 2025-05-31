package org.dfpl.lecture.db.backend.dto;

import lombok.*;

import java.util.List;

@Getter @Builder @AllArgsConstructor
public class MovieDetailDTO {

    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private Integer runtime;                 // 분 단위
    private List<String> genres;

    private String posterPath;               // /w500/....jpg
    private String backdropPath;

    /* 인물 & 미디어 */
    private List<CastDTO> casts;             // 상위 10명
    private List<CastDTO> directors;         // 감독(들)
    private List<ImageDTO> gallery;          // 백드롭 10장
    private List<VideoDTO> videos;           // 예고편 etc.
}
