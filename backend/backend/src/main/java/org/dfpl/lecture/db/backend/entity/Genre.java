package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dfpl.lecture.db.backend.dto.GenreDTO;

import java.util.List;

@Entity
@Table(name = "genre_v3")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    /**
     * TMDB가 제공하는 장르 ID를 그대로 PK로 사용합니다.
     * 예: 28=액션, 12=어드벤처, 35=코미디 등
     */
    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}
