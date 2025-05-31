package org.dfpl.lecture.db.backend.dto;

import lombok.*;

@Getter @Builder @AllArgsConstructor
public class ImageDTO {
    private String filePath;    // /original/....jpg
    private Integer width;
    private Integer height;
}
