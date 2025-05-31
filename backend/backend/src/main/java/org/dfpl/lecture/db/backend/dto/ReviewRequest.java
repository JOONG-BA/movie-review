package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long movieId;
    private int score;       // 1~5
    private String content;
}
