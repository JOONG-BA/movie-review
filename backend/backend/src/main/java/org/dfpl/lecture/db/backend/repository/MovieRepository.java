package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieDB, Long> {

    /**
     * 특정 장르 ID (genre.id) 를 가진 영화들을
     * voteCount(투표 수) 기준 내림차순(인기순)으로 페이징 조회합니다.
     */
    Page<MovieDB> findByGenres_IdOrderByVoteCountDesc(Long genreId, Pageable pageable);

    // 글로벌 인기순(페이징) 조회: 기본 findAll(Pageable) + Sort 사용 가능
    // List<MovieDB> findAllByOrderByVoteCountDesc(Pageable pageable);
}
