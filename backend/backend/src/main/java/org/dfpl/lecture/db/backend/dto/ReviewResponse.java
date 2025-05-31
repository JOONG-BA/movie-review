package org.dfpl.lecture.db.backend.dto;

public record ReviewResponse(
        String movieTitle,
        int    score,
        String content
) {}
