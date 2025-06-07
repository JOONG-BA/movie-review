package org.dfpl.lecture.db.backend.dto;

public record ReviewResponse(
        String movieTitle,
        Double    score,
        String content,
        String authorNickname
) {}
