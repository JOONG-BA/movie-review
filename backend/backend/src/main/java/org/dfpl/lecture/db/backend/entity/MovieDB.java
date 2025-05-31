package org.dfpl.lecture.db.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dfpl.lecture.db.backend.dto.MovieSearchResultDTO;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "movie_db", indexes = {
        @Index(name = "idx_tmdb_id", columnList = "tmdb_id", unique = true)
})
public class MovieDB {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Long tmdbId;

    private String title;
    private String posterPath;
    private String releaseDate;
    private Double popularity;

    /** TMDb 검색 DTO → 엔티티 */
    public static MovieDB fromSearchDTO(MovieSearchResultDTO dto) {
        return MovieDB.builder()
                .tmdbId(dto.getTmdbId())
                .title(dto.getTitle())
                .posterPath(dto.getPosterPath())
                .releaseDate(dto.getReleaseDate())
                .popularity(dto.getPopularity())
                .build();
    }
}
