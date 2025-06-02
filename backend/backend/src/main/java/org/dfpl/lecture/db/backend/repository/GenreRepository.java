package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
