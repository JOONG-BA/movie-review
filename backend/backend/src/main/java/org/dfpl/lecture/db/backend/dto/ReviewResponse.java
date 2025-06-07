package org.dfpl.lecture.db.backend.dto;

public record ReviewResponse(
        String movieTitle,
        Integer    score,
        String content,
        String authorNickname
) {}
