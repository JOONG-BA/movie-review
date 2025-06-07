package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class MyReviewDTO {
    private Long reviewId;
    private Long movieId;
    private Double score;
    private String content;

    public MyReviewDTO(Long reviewId, Long movieId, Double score, String content) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.score = score;
        this.content = content;
    }
}
