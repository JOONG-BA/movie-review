package org.dfpl.lecture.db.backend.dto;

public record ReviewDTO(
        Long id,
        Long movieId,
        int score,
        String content
) {}
