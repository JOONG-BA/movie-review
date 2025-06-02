package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.GenreDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.CastDTO;
import org.dfpl.lecture.db.backend.dto.MovieSummaryDTO;
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

        // 이미 DB에 존재하면 저장하지 않음
        if (movieRepository.existsByTmdbId(tmdbId)) {
            return;
        }

        // TMDb API에서 상세정보 받아오기
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

            // 기존 관계 초기화
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
            // 실제 GenreDTO 클래스의 필드가 (id, name) 이어야 합니다.
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
            // 실제 CastDTO 클래스에 "getCastId()"가 아닌 "getId()" 또는 다른 getter가 있는지 확인하세요.
            // 예를 들어 CastDTO 필드명이 "id" 라면 아래를 "c.getId()" 로 바꿔야 컴파일 에러가 사라집니다.
            Long castIdFromDTO = c.getId(); // ➔ 만약 실제 getter가 getId()라면 이렇게!
            Cast cast = castRepository.findById(castIdFromDTO)
                    .orElseGet(() -> new Cast(castIdFromDTO, c.getName(), c.getProfilePath()));
            cast.setName(c.getName());
            cast.setProfilePath(c.getProfilePath());
            castRepository.save(cast);

            MovieCast mc = new MovieCast(movie, cast, c.getCharacterName());
            movie.getMovieCasts().add(mc);
            cast.getMovieCasts().add(mc);
        }

        // 4) 최종 저장 (Cascade 옵션으로 MovieGenre, MovieCast도 함께 저장됨)
        movieRepository.save(movie);
    }

    /**
     * 한국에서 상영된 영화 중 인기도 순 상위 N개를 가져와 DTO 리스트로 변환
     * → MovieSummaryDTO.genres 필드는 List<String> 으로 가정
     */
    public List<MovieSummaryDTO> getTopNPopularInKorea(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository.findByReleasedInKoreaTrueOrderByPopularityDesc(pageable);

        return movies.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * 특정 장르 ID에 속하며 한국에서 상영된 영화 중 인기도 순 상위 N개 가져오기
     */
    public List<MovieSummaryDTO> getTopNPopularInKoreaByGenre(Long genreId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository
                .findByReleasedInKoreaTrueAndMovieGenres_Genre_IdOrderByPopularityDesc(genreId, pageable);

        return movies.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * 검색 시 API 요약 → DB 저장 확인 → DB 기반 Summary 반환
     *   (MovieSummaryDTO.genres 는 List<String>)
     */
    @Transactional
    public List<MovieSummaryDTO> searchAndSaveIfMissing(String query, int limit) {
        List<MovieSummaryDTO> apiSummaries;
        try {
            apiSummaries = tmdbApiUtil.searchMovies(query);
        } catch (Exception e) {
            System.err.println("[영화 검색 실패: query=" + query + "] " + e.getMessage());
            return List.of();
        }

        List<MovieSummaryDTO> finalResults = new ArrayList<>();

        for (int i = 0; i < apiSummaries.size() && finalResults.size() < limit; i++) {
            MovieSummaryDTO apiSummary = apiSummaries.get(i);
            Long tmdbId = apiSummary.getTmdbId();

            // DB에 없으면 상세 정보 저장
            if (!movieRepository.existsByTmdbId(tmdbId)) {
                saveMovieDetail(tmdbId);
            }

            // DB에서 엔티티를 꺼내 변환
            MovieDB movie = movieRepository.findByTmdbId(tmdbId);
            if (movie == null) {
                // 저장 실패 시, API 요약 정보만 활용 (genres는 빈 List)
                finalResults.add(
                        MovieSummaryDTO.builder()
                                .tmdbId(apiSummary.getTmdbId())
                                .title(apiSummary.getTitle())
                                .posterPath(apiSummary.getPosterPath())
                                .popularity(apiSummary.getPopularity())
                                .genres(List.of()) // 빈 List<String>
                                .build()
                );
            } else {
                finalResults.add(toSummaryDTO(movie));
            }
        }

        return finalResults;
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // 공통 변환 메서드: MovieDB → MovieSummaryDTO
    //   MovieSummaryDTO.genres 의 타입은 List<String> 으로 가정
    // ────────────────────────────────────────────────────────────────────────────────

    private MovieSummaryDTO toSummaryDTO(MovieDB movie) {
        List<String> genreNames = movie.getMovieGenres().stream()
                .map(MovieGenre::getGenre)
                .map(Genre::getName)
                .collect(Collectors.toList());

        return MovieSummaryDTO.builder()
                .tmdbId(movie.getTmdbId())
                .title(movie.getTitle())
                .posterPath(movie.getPosterPath())
                .popularity(movie.getPopularity())
                .genres(genreNames)  // List<String>
                .build();
    }

    /**
     * 영화 상세조회: DB에서 MovieDB 꺼내서 MovieDetailDTO로 변환
     *   MovieDetailDTO.genres 의 타입은 List<GenreDTO>
     */
    public MovieDetailDTO getMovieDetailByTmdbId(Long tmdbId) {
        MovieDB movie = movieRepository.findByTmdbId(tmdbId);
        if (movie == null) {
            return null;
        }

        // 1) 장르 목록 → GenreDTO 리스트로 변환
        List<GenreDTO> genreDTOs = movie.getMovieGenres().stream()
                .map(MovieGenre::getGenre)      // MovieGenre → Genre 엔티티
                .map(g -> GenreDTO.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .build())
                .collect(Collectors.toList());

        // 2) 캐스트 목록 → CastDTO 리스트로 변환
        List<CastDTO> castList = movie.getMovieCasts().stream()
                .map(mc -> {
                    // Builder 메서드는 CastDTO 필드명에 맞춰 수정하세요.
                    // 예: 필드명이 "id" 라면 builder().id(...) 로, "castId" 라면 .castId(...) 로 바꿔야 합니다.
                    return CastDTO.builder()
                            .id(mc.getCast().getId())               // 실제 Cast 엔티티의 ID
                            .name(mc.getCast().getName())
                            .characterName(mc.getCharacterName())
                            .profilePath(mc.getCast().getProfilePath())
                            .build();
                })
                .collect(Collectors.toList());

        // 3) MovieDetailDTO 생성 및 반환
        return MovieDetailDTO.builder()
                .tmdbId(movie.getTmdbId())
                .title(movie.getTitle())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .originalLanguage(movie.getOriginalLanguage())
                .releaseDate(movie.getReleaseDate())
                .releasedInKorea(movie.getReleasedInKorea())
                .releaseDateKorea(movie.getReleaseDateKorea())
                .runtime(movie.getRuntime())
                .popularity(movie.getPopularity())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .budget(movie.getBudget())
                .revenue(movie.getRevenue())
                .status(movie.getStatus())
                .tagline(movie.getTagline())
                .homepage(movie.getHomepage())
                .backdropPath(movie.getBackdropPath())
                .posterPath(movie.getPosterPath())
                .genres(genreDTOs)   // List<GenreDTO>
                .casts(castList)     // List<CastDTO>
                .build();
    }
}
