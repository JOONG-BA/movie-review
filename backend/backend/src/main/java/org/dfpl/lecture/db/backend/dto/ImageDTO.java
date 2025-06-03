package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class ImageDTO {
    private String type; // "backdrop" or "poster"
    private String url;
}
