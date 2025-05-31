package org.dfpl.lecture.db.backend.dto;

import lombok.*;

@Getter @Builder @AllArgsConstructor
public class VideoDTO {
    private String key;     // YouTube key 등
    private String site;    // "YouTube"
    private String name;    // "Official Trailer"
    private String type;    // "Trailer", "Teaser" ...
}
