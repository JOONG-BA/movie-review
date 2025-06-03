package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private String name; // 예고편 이름(e.g. "Official Trailer")
    private String url;  // "https://www.youtube.com/watch?v={key}"
}
