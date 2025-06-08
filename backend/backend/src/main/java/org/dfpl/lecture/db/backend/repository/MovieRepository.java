package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieDB, Long> {

    /**
     * 1) 전체를 popularity 내림차순으로 페이징 조회
     *    (컨트롤러에서 Pageable에 Sort.by("popularity").descending()을 설정해도 무방합니다.)
     */
    Page<MovieDB> findAllByOrderByPopularityDesc(Pageable pageable);

    /**
     * 2) 특정 장르 ID(genre1~4 칼럼 중 하나에 해당)인 영화만 뽑아서
     *    popularity 내림차순으로 페이징 조회
     */
    @Query("""
        SELECT m
          FROM MovieDB m
          JOIN m.movieGenres mg
         WHERE mg.genre.id = :genreId
         ORDER BY m.popularity DESC
        """)
    Page<MovieDB> findByGenreIdOrderByPopularityDesc(
            @Param("genreId") Long genreId,
            Pageable pageable
    );

    /**
     * 3) 제목(title) 또는 개요(overview)에 keyword가 포함된 영화를
     *    popularity 내림차순으로 페이징 조회
     */
    @Query("""
        SELECT m
        FROM MovieDB m
        WHERE (m.title)   LIKE (CONCAT('%', :kw, '%'))
        ORDER BY m.popularity DESC
        """)
    Page<MovieDB> searchByKeywordOrderByPopularityDesc(
            @Param("kw") String keyword,
            Pageable pageable
    );
}
