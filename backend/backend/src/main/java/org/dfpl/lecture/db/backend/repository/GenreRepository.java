package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    // 추가적인 쿼리가 필요한 경우, 여기다 메서드를 정의합니다.
    // 예: Optional<Genre> findByName(String name);
}
