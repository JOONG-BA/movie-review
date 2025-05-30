package org.dfpl.lecture.db.backend.dto;

public record MyReviewDTO(
        Long id,
        Long movieId,
        int score,
        String content
) {}
