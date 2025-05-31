package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieDB, Long> {

    /* 공백·대소문자 무시 + 연관성 → 인기순 정렬 */
    @Query(value = """
        SELECT  m.*
        FROM    movie_db m
        WHERE   LOWER(REPLACE(m.title, ' ', ''))
                LIKE CONCAT('%', :norm, '%')
        ORDER BY
            CASE
                WHEN LOWER(REPLACE(m.title, ' ', '')) = :norm THEN 3
                WHEN LOWER(REPLACE(m.title, ' ', '')) LIKE CONCAT(:norm, '%') THEN 2
                ELSE 1
            END DESC,
            m.popularity DESC
        """, nativeQuery = true)
    List<MovieDB> searchByRelevance(@Param("norm") String normalized);
    boolean existsByTmdbId(Long tmdbId);

    Optional<MovieDB> findByTmdbId(Long tmdbId);
}
