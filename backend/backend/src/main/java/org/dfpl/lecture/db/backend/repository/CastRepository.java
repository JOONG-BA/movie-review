package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.Cast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastRepository extends JpaRepository<Cast, Long> {
}
