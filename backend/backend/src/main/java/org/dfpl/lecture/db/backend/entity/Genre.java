package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    private Long id;           // TMDb상의 genre ID

    private String name;       // ex) "액션", "드라마" 등

    // MovieGenre와의 관계
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MovieGenre> movieGenres = new HashSet<>();

    /**
     * 2-argument 생성자를 직접 호출할 때 필요한 시그니처
     * 이 생성자에서 movieGenres를 반드시 초기화해야 합니다.
     */
    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
        // 연관관계 컬렉션도 빈 HashSet으로 초기화
        this.movieGenres = new HashSet<>();
    }
}
