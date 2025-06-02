package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieDB, Long> {
    boolean existsByTmdbId(Long tmdbId);
    MovieDB findByTmdbId(Long tmdbId);
}
