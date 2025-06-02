package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_casts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"movie", "cast"})
public class MovieCast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    private MovieDB movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cast_id")
    private Cast cast;

    private String characterName;   // 배우가 맡은 배역명 (한국어 or fallback)

    public MovieCast(MovieDB movie, Cast cast, String characterName) {
        this.movie = movie;
        this.cast = cast;
        this.characterName = characterName;
    }
}
