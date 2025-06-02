package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieDB, Long> {
    boolean existsByTmdbId(Long tmdbId);
    MovieDB findByTmdbId(Long tmdbId);

    /**
     * 1) 한국에서 상영된 영화(true) 중에서 인기 순으로 정렬해서 가져오기
     *    (Pageable에 pageSize만 지정하면 Top N이 됨)
     */
    List<MovieDB> findByReleasedInKoreaTrueOrderByPopularityDesc(Pageable pageable);

    /**
     * 2) 특정 장르(genreId)가 포함되면서, 한국 개봉(true)된 영화 중 인기도 내림차순 정렬
     *    (MovieDB.movieGenres.장르 엔티티와 조인해서 검색)
     */
    List<MovieDB> findByReleasedInKoreaTrueAndMovieGenres_Genre_IdOrderByPopularityDesc(
            Long genreId, Pageable pageable);
}
