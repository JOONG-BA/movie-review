package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.CastDTO;
import org.dfpl.lecture.db.backend.dto.MovieDetailDTO.GenreDTO;
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

    // ─────────────────────────────────────────────────────────────────
    // (1) TMDb 인기 영화 목록(page 단위)에서 ID 가져오기 → DB 저장 (스케줄러용)
    // ─────────────────────────────────────────────────────────────────
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

    // ─────────────────────────────────────────────────────────────────
    // (2) 단일 TMDb 영화 ID에 대해
    //     1) TMDb API에서 MovieDetailDTO 받아오기
    //     2) MovieDB 엔티티 생성/업데이트
    //     3) Genre 저장 → MovieGenre 관계 생성
    //     4) Cast 저장 → MovieCast 관계 생성
    // ─────────────────────────────────────────────────────────────────
    @Transactional
    public void saveMovieDetail(Long tmdbId) {
        // 이미 DB에 같은 tmdbId가 있으면 중복 저장하지 않음
        if (movieRepository.existsByTmdbId(tmdbId)) {
            return;
        }

        // 1) TMDb에서 상세정보(Credits, Release Dates, Images, Videos)를 포함한 MovieDetailDTO 받아오기
        MovieDetailDTO detailDTO;
        try {
            detailDTO = tmdbApiUtil.fetchMovieDetail(tmdbId);
        } catch (Exception e) {
            System.err.println("[TMDb ID=" + tmdbId + "] 상세정보 호출 실패: " + e.getMessage());
            return;
        }

        // 2) MovieDB 엔티티 조회 또는 신규 생성
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

            // 기존 관계 엔티티 초기화 (Cascade 옵션으로, DB에서 자동으로 영속성 관리됨)
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

        // 3) Genre 저장 및 MovieGenre 관계 생성
        for (GenreDTO g : detailDTO.getGenres()) {
            Genre genre = genreRepository.findById(g.getId())
                    .orElseGet(() -> new Genre(g.getId(), g.getName()));
            genre.setName(g.getName());
            genreRepository.save(genre);

            MovieGenre mg = new MovieGenre(movie, genre);
            movie.getMovieGenres().add(mg);
            genre.getMovieGenres().add(mg);
        }

        // 4) Cast 저장 및 MovieCast 관계 생성
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

        // 5) 최종 저장 (Cascade 옵션으로 MovieGenre, MovieCast 모두 함께 저장)
        movieRepository.save(movie);
    }

    // ─────────────────────────────────────────────────────────────────
    // (3) 검색 시
    //     - TMDb 검색 API를 이용해 요약 정보(MovieSummaryDTO) 리스트 가져오기
    //     - DB에 없으면 saveMovieDetail() 호출해서 상세 저장
    //     - 최종적으로 DB에서 다시 꺼내서 MovieSummaryDTO 반환
    // ─────────────────────────────────────────────────────────────────
    @Transactional
    public List<MovieSummaryDTO> searchAndSaveIfMissing(String query, int limit) {
        // TMDb에서 “영화 검색” (요약 정보만) 가져오기
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

            // (3-1) DB에 없으면 저장
            if (!movieRepository.existsByTmdbId(tmdbId)) {
                saveMovieDetail(tmdbId);
            }

            // (3-2) DB에서 MovieDB 엔티티 꺼내서 Summary → 반환 리스트에 담기
            MovieDB movie = movieRepository.findByTmdbId(tmdbId);
            if (movie != null) {
                finalResults.add(toSummaryDTO(movie));
            } else {
                // 저장 실패 케이스 → API에서 받은 간단 요약 정보만 담아서 반환
                finalResults.add(
                        MovieSummaryDTO.builder()
                                .tmdbId(apiSummary.getTmdbId())
                                .title(apiSummary.getTitle())
                                .posterPath(apiSummary.getPosterPath())
                                .popularity(apiSummary.getPopularity())
                                .genres(List.of())  // 저장 실패 시 빈 장르
                                .build()
                );
            }
        }

        return finalResults;
    }

    // ─────────────────────────────────────────────────────────────────
    // (4) 한국에서 상영된 영화 중 인기도 순 상위 N개 가져오기
    // ─────────────────────────────────────────────────────────────────
    public List<MovieSummaryDTO> getTopNPopularInKorea(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository.findByReleasedInKoreaTrueOrderByPopularityDesc(pageable);

        return movies.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────
    // (5) 특정 장르 ID에 속하며, 한국 개봉된 영화 중 인기도 순 상위 N개 가져오기
    // ─────────────────────────────────────────────────────────────────
    public List<MovieSummaryDTO> getTopNPopularInKoreaByGenre(Long genreId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies = movieRepository
                .findByReleasedInKoreaTrueAndMovieGenres_Genre_IdOrderByPopularityDesc(genreId, pageable);

        return movies.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────
    // (6) DB에 저장된 MovieDB 엔티티 → MovieSummaryDTO 변환 (장르 이름 목록 포함)
    // ─────────────────────────────────────────────────────────────────
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
                .genres(genreNames)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────
    // (7) DB에 저장된 MovieDB 엔티티 → MovieDetailDTO 변환
    //     (사용 예: GET /api/movies/{tmdbId} → 상세 조회)
    // ─────────────────────────────────────────────────────────────────
    public MovieDetailDTO getMovieDetailByTmdbId(Long tmdbId) {
        MovieDB movie = movieRepository.findByTmdbId(tmdbId);
        if (movie == null) {
            return null;
        }

        // 7-1) DB에서 꺼낸 MovieDB의 Genre 관계를 GenreDTO로 변환
        List<GenreDTO> genreDTOs = movie.getMovieGenres().stream()
                .map(MovieGenre::getGenre)
                .map(g -> GenreDTO.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .build())
                .collect(Collectors.toList());

        // 7-2) DB에서 꺼낸 MovieDB의 Cast 관계를 CastDTO로 변환
        List<CastDTO> castList = movie.getMovieCasts().stream()
                .map(mc -> CastDTO.builder()
                        .id(mc.getCast().getId())
                        .name(mc.getCast().getName())
                        .characterName(mc.getCharacterName())
                        .profilePath(mc.getCast().getProfilePath())
                        .build())
                .collect(Collectors.toList());

        // 7-3) “hasImages” / “hasVideos” 정보는 MovieDB 엔티티에 저장된 필드가 없으므로
        //       API 호출 시점의 정보를 DB에 보관하지 않았다면, false 또는 null로 설정해 둡니다.
        //       필요하다면 Entity 쪽에 hasImages/hasVideos 칼럼을 추가해서 저장할 수도 있습니다.
        Boolean hasImages = null;  // 또는 false
        Boolean hasVideos = null;  // 또는 false

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
                .genres(genreDTOs)
                .casts(castList)
                .hasImages(hasImages)
                .hasVideos(hasVideos)
                .build();
    }
}
