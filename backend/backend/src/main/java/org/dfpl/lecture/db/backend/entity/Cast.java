package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "casts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cast {

    @Id
    private Long id;               // TMDb상의 cast ID

    private String name;           // 배우 이름 (한국어 or fallback)
    private String profilePath;    // 프로필 이미지 경로 ("/…jpg")

    @OneToMany(mappedBy = "cast", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MovieCast> movieCasts = new HashSet<>();

    /**
     * 3-argument 생성자(앱에서 직접 호출할 때)
     * 이 생성자에서 movieCasts를 반드시 초기화해야 합니다.
     */
    public Cast(Long id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
        // 연관관계 컬렉션도 빈 HashSet으로 초기화
        this.movieCasts = new HashSet<>();
    }
}
