package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long movieId;
    private Integer score;    // null 허용
    private String content;
}
