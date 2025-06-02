package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.CastDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.GenreDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
import org.dfpl.lecture.db.backend.entity.*;
import org.dfpl.lecture.db.backend.repository.*;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    /**
     * 한국에서 상영된 영화 중에서 인기도 순 상위 N개를 가져와 DTO 리스트로 변환
     */
    public List<MovieSummaryDTO> getTopNPopularInKorea(int limit) {
        // Pageable: 첫 페이지(0), size = limit, 정렬은 Repository 메서드가 이미 popularity desc 순서로 처리
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository.findByReleasedInKoreaTrueOrderByPopularityDesc(pageable);

        return movies.stream()
                .map(m -> MovieSummaryDTO.builder()
                        .tmdbId(m.getTmdbId())
                        .title(m.getTitle())
                        .posterPath(m.getPosterPath())
                        .popularity(m.getPopularity())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 특정 장르 ID에 속하며, 한국에서 상영된 영화 중 인기도 순 상위 N개를 가져오는 메서드
     */
    public List<MovieSummaryDTO> getTopNPopularInKoreaByGenre(Long genreId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository
                .findByReleasedInKoreaTrueAndMovieGenres_Genre_IdOrderByPopularityDesc(genreId, pageable);

        return movies.stream()
                .map(m -> MovieSummaryDTO.builder()
                        .tmdbId(m.getTmdbId())
                        .title(m.getTitle())
                        .posterPath(m.getPosterPath())
                        .popularity(m.getPopularity())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * (추가) 검색 시 API 요약 결과를 먼저 가져오고, DB에 없으면 상세 가져와 저장 → 최종적으로 DB 기반으로 MovieSummaryDTO 반환
     *
     * @param query 키워드 (예: "어벤져스")
     * @param limit 최대 몇 개까지 결과를 돌려줄지 (예: 10)
     * @return MovieSummaryDTO 목록 (tmdbId, title, posterPath, popularity 포함)
     */
    @Transactional
    public List<MovieSummaryDTO> searchAndSaveIfMissing(String query, int limit) {
        List<MovieSummaryDTO> apiSummaries;
        try {
            // 1) TMDb 검색 API를 호출해 요약 정보 리스트를 가져온다.
            apiSummaries = tmdbApiUtil.searchMovies(query);
        } catch (Exception e) {
            System.err.println("[영화 검색 실패: query=" + query + "] " + e.getMessage());
            return List.of();
        }

        List<MovieSummaryDTO> finalResults = new ArrayList<>();

        // 2) 검색 결과 중에서 최대 limit개까지만 처리
        for (int i = 0; i < apiSummaries.size() && finalResults.size() < limit; i++) {
            MovieSummaryDTO apiSummary = apiSummaries.get(i);
            Long tmdbId = apiSummary.getTmdbId();

            // 2-1) DB에 없으면 saveMovieDetail로 상세정보 + 저장
            if (!movieRepository.existsByTmdbId(tmdbId)) {
                saveMovieDetail(tmdbId);
            }

            // 2-2) DB에서 엔티티를 꺼내어 Summary DTO로 변환
            MovieDB movie = movieRepository.findByTmdbId(tmdbId);
            if (movie == null) {
                // 만약 saveMovieDetail에서 실패해 DB에 없다면, API 요약 정보(apiSummary)를 그대로 넣는다.
                finalResults.add(apiSummary);
            } else {
                // DB에 정상 저장된 엔티티 기반으로 MovieSummaryDTO 생성
                MovieSummaryDTO summary = MovieSummaryDTO.builder()
                        .tmdbId(movie.getTmdbId())
                        .title(movie.getTitle())
                        .posterPath(movie.getPosterPath())
                        .popularity(movie.getPopularity())
                        .build();
                finalResults.add(summary);
            }
        }

        return finalResults;
    }
}
