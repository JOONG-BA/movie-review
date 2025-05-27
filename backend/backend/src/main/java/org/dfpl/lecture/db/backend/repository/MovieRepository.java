package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieDB, Long> {
    List<MovieDB> findByTitleContainingIgnoreCase(String title);
}
