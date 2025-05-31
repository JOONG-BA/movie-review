package org.dfpl.lecture.db.backend.dto;

import lombok.*;

@Getter @Builder @AllArgsConstructor
public class CastDTO {
    private Long id;
    private String name;
    private String role;        // character or job
    private String profilePath; // /w185/....jpg
}
