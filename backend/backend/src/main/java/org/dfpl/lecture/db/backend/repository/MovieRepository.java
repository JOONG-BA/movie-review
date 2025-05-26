package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieDB, Long> {
}
