package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.CastDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.GenreDTO;
import org.dfpl.lecture.db.backend.entity.Cast;
import org.dfpl.lecture.db.backend.entity.Genre;
import org.dfpl.lecture.db.backend.entity.MovieCast;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.MovieGenre;
import org.dfpl.lecture.db.backend.repository.CastRepository;
import org.dfpl.lecture.db.backend.repository.GenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieCastRepository;
import org.dfpl.lecture.db.backend.repository.MovieGenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbApiUtil tmdbApiUtil;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CastRepository castRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieCastRepository movieCastRepository;

    /**
     * 1) TMDb 인기 영화 목록(page 단위)에서 ID 가져오기
     * 2) 각 ID마다 TMDb 세부정보 크롤링 → DB 저장
     */
    @Transactional
    public void crawlPopularMovies(int totalPages) {
        for (int page = 1; page <= totalPages; page++) {
            List<Long> movieIds;
            try {
                movieIds = tmdbApiUtil.fetchPopularMovieIds(page);
            } catch (Exception e) {
                System.err.println("[" + page + " 페이지] 인기 영화 ID 호출 실패: " + e.getMessage());
                continue;
            }

            for (Long tmdbId : movieIds) {
                saveMovieDetail(tmdbId);
            }
        }
    }

    /**
     * 단일 TMDb 영화 ID에 대해
     *  1) TMDb API에서 MovieDetailDTO 받아오기
     *  2) MovieDB 엔티티 생성/업데이트
     *  3) Genre 저장 → MovieGenre 관계 생성
     *  4) Cast 저장 → MovieCast 관계 생성
     */
    @Transactional
    public void saveMovieDetail(Long tmdbId) {

        if (movieRepository.existsByTmdbId(tmdbId)) {
            return;
        }

        MovieDetailDTO detailDTO;
        try {
            detailDTO = tmdbApiUtil.fetchMovieDetail(tmdbId);
        } catch (Exception e) {
            System.err.println("[TMDb ID=" + tmdbId + "] 상세정보 호출 실패: " + e.getMessage());
            return;
        }

        // 1) MovieDB 엔티티 조회 또는 신규 생성
        Optional<MovieDB> maybeMovie = movieRepository.findAll()
                .stream()
                .filter(m -> m.getTmdbId().equals(detailDTO.getTmdbId()))
                .findFirst();

        MovieDB movie;
        if (maybeMovie.isPresent()) {
            // 기존 영화 → 업데이트 모드
            movie = maybeMovie.get();
            movie.setTitle(detailDTO.getTitle());
            movie.setOverview(detailDTO.getOverview());
            movie.setOriginalTitle(detailDTO.getOriginalTitle());
            movie.setOriginalLanguage(detailDTO.getOriginalLanguage());
            movie.setReleaseDate(detailDTO.getReleaseDate());
            movie.setRuntime(detailDTO.getRuntime());
            movie.setPopularity(detailDTO.getPopularity());
            movie.setVoteAverage(detailDTO.getVoteAverage());
            movie.setVoteCount(detailDTO.getVoteCount());
            movie.setStatus(detailDTO.getStatus());
            movie.setTagline(detailDTO.getTagline());
            movie.setBudget(detailDTO.getBudget());
            movie.setRevenue(detailDTO.getRevenue());
            movie.setHomepage(detailDTO.getHomepage());
            movie.setBackdropPath(detailDTO.getBackdropPath());
            movie.setPosterPath(detailDTO.getPosterPath());
            movie.setReleasedInKorea(detailDTO.getReleasedInKorea());
            movie.setReleaseDateKorea(detailDTO.getReleaseDateKorea());

            // 기존 관계 엔티티 초기화
            movie.getMovieGenres().clear();
            movie.getMovieCasts().clear();
        } else {
            // 신규 생성
            movie = MovieDB.builder()
                    .tmdbId(detailDTO.getTmdbId())
                    .title(detailDTO.getTitle())
                    .overview(detailDTO.getOverview())
                    .originalTitle(detailDTO.getOriginalTitle())
                    .originalLanguage(detailDTO.getOriginalLanguage())
                    .releaseDate(detailDTO.getReleaseDate())
                    .runtime(detailDTO.getRuntime())
                    .popularity(detailDTO.getPopularity())
                    .voteAverage(detailDTO.getVoteAverage())
                    .voteCount(detailDTO.getVoteCount())
                    .status(detailDTO.getStatus())
                    .tagline(detailDTO.getTagline())
                    .budget(detailDTO.getBudget())
                    .revenue(detailDTO.getRevenue())
                    .homepage(detailDTO.getHomepage())
                    .backdropPath(detailDTO.getBackdropPath())
                    .posterPath(detailDTO.getPosterPath())
                    .releasedInKorea(detailDTO.getReleasedInKorea())
                    .releaseDateKorea(detailDTO.getReleaseDateKorea())
                    .build();
        }

        // 2) Genre 저장 및 MovieGenre 관계 생성
        for (GenreDTO g : detailDTO.getGenres()) {
            // 2-arg 생성자를 통해 Genre 엔티티를 만들거나, DB에서 조회
            Genre genre = genreRepository.findById(g.getId())
                    .orElseGet(() -> new Genre(g.getId(), g.getName()));
            genre.setName(g.getName());
            genreRepository.save(genre);

            MovieGenre mg = new MovieGenre(movie, genre);
            movie.getMovieGenres().add(mg);
            genre.getMovieGenres().add(mg);
        }

        // 3) Cast 저장 및 MovieCast 관계 생성
        for (CastDTO c : detailDTO.getCasts()) {
            Cast cast = castRepository.findById(c.getId())
                    .orElseGet(() -> new Cast(c.getId(), c.getName(), c.getProfilePath()));
            cast.setName(c.getName());
            cast.setProfilePath(c.getProfilePath());
            castRepository.save(cast);

            MovieCast mc = new MovieCast(movie, cast, c.getCharacterName());
            movie.getMovieCasts().add(mc);
            cast.getMovieCasts().add(mc);
        }

        // 4) 최종 저장 (Cascade 옵션으로 관계 엔티티도 함께 저장)
        movieRepository.save(movie);
    }
}
