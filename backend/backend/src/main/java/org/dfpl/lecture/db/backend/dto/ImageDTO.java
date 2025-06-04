package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private String url;  // TmdbApiUtil.getImageUrl(filePath) 결과
}
