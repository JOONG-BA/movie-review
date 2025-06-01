package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class MyReviewDTO {
    private Long reviewId;
    private Long movieId;
    private int score;
    private String content;

    public MyReviewDTO(Long reviewId, Long movieId, int score, String content) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.score = score;
        this.content = content;
    }
}
