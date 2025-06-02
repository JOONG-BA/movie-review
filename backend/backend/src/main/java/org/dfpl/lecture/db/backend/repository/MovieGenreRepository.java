package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
}
