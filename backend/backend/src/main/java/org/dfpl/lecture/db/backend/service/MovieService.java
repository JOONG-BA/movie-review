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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final TmdbApiUtil tmdbApiUtil;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final CastRepository castRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieCastRepository movieCastRepository;

    // (1) TMDb 인기 영화 한 페이지를 가져와서 DB에 저장
    //     POST /api/movies/fetch?page={page}
    @Transactional
    public List<MovieDB> fetchAndSavePopularMovies(int page) {
        List<Long> movieIds;
        try {
            movieIds = tmdbApiUtil.fetchPopularMovieIds(page);
        } catch (Exception e) {
            System.err.println("[Page " + page + "] 인기 영화 ID 호출 실패: " + e.getMessage());
            return Collections.emptyList();
        }

        List<MovieDB> savedList = new ArrayList<>();
        for (Long tmdbId : movieIds) {
            saveMovieDetail(tmdbId);
            MovieDB saved = movieRepository.findByTmdbId(tmdbId);
            if (saved != null) {
                savedList.add(saved);
            }
        }
        return savedList;
    }

    // (2) 여러 페이지(fromPage ~ toPage)를 한 번에 가져와서 DB에 저장
    //     POST /api/movies/fetch-range?from={from}&to={to}
    @Transactional
    public List<MovieDB> fetchAndSavePopularMovies(int fromPage, int toPage) {
        List<MovieDB> allSaved = new ArrayList<>();
        for (int p = fromPage; p <= toPage; p++) {
            List<MovieDB> savedOnPage = fetchAndSavePopularMovies(p);
            allSaved.addAll(savedOnPage);
        }
        return allSaved;
    }

    // (3) TMDb 실시간 검색 + DB에 없으면 저장
    //     GET /api/movies/search?query={검색어}
    @Transactional
    public List<MovieSummaryDTO> searchAndSaveIfMissing(String query, int limit) {
        List<MovieSummaryDTO> apiSummaries;
        try {
            apiSummaries = tmdbApiUtil.searchMovies(query);
        } catch (Exception e) {
            System.err.println("[영화 검색 실패: query=" + query + "] " + e.getMessage());
            return Collections.emptyList();
        }

        List<MovieSummaryDTO> finalResults = new ArrayList<>();
        for (MovieSummaryDTO apiSummary : apiSummaries) {
            Long tmdbId = apiSummary.getTmdbId();
            if (!movieRepository.existsByTmdbId(tmdbId)) {
                saveMovieDetail(tmdbId);
            }
            MovieDB movie = movieRepository.findByTmdbId(tmdbId);
            if (movie != null) {
                finalResults.add(toSummaryDTO(movie));
            }
            if (finalResults.size() >= limit) {
                break;
            }
        }
        return finalResults;
    }

    // (4) 개별 영화 상세 조회
    //     GET /api/movies/{tmdbId}
    @Transactional(readOnly = true)
    public MovieDetailDTO getDetail(Long tmdbId) {
        if (!movieRepository.existsByTmdbId(tmdbId)) {
            saveMovieDetail(tmdbId);
        }
        MovieDB movie = movieRepository.findByTmdbId(tmdbId);
        if (movie == null) {
            return null;
        }
        return toDetailDTO(movie);
    }

    // (5) 특정 장르 ID에 속하며, 한국 개봉된 영화 중 인기도 TOP N
    //     GET /api/movies/genre/{genreId}?limit={개수}
    @Transactional(readOnly = true)
    public List<MovieSummaryDTO> getTopNPopularInKoreaByGenre(Long genreId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<MovieDB> movies =
                movieRepository.findByReleasedInKoreaTrueAndMovieGenres_Genre_IdOrderByPopularityDesc(
                        genreId, pageable);
        return movies.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    // (A) 실제 DB에 상세 정보를 저장하는 메서드
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

        MovieDB movie = MovieDB.builder()
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

        // Genre 저장 및 연관관계 MovieGenre 생성
        for (GenreDTO g : detailDTO.getGenres()) {
            Genre genre = genreRepository.findById(g.getId())
                    .orElseGet(() -> new Genre(g.getId(), g.getName()));
            genre.setName(g.getName());
            genreRepository.save(genre);

            MovieGenre mg = new MovieGenre(movie, genre);
            movie.getMovieGenres().add(mg);
            genre.getMovieGenres().add(mg);
        }

        // Cast 저장 및 연관관계 MovieCast 생성
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

        movieRepository.save(movie);
    }

    // (B) Entity → MovieSummaryDTO 변환
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

    // (C) Entity → MovieDetailDTO 변환
    private MovieDetailDTO toDetailDTO(MovieDB movie) {
        Long id = movie.getTmdbId();
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String originalTitle = movie.getOriginalTitle();
        String originalLanguage = movie.getOriginalLanguage();
        LocalDate releaseDate = movie.getReleaseDate();
        Integer runtime = movie.getRuntime();
        Double popularity = movie.getPopularity();
        Double voteAverage = movie.getVoteAverage();
        Integer voteCount = movie.getVoteCount();
        String status = movie.getStatus();
        String tagline = movie.getTagline();
        Long budget = movie.getBudget();
        Long revenue = movie.getRevenue();
        String homepage = movie.getHomepage();
        String backdropPath = movie.getBackdropPath();
        String posterPath = movie.getPosterPath();
        Boolean releasedInKorea = movie.getReleasedInKorea();
        LocalDate releaseDateKorea = movie.getReleaseDateKorea();

        List<GenreDTO> genreDTOs = movie.getMovieGenres().stream()
                .map(mg -> new GenreDTO(
                        mg.getGenre().getId(),
                        mg.getGenre().getName()
                ))
                .collect(Collectors.toList());

        List<CastDTO> castList = movie.getMovieCasts().stream()
                .map(mc -> new CastDTO(
                        mc.getCast().getId(),
                        mc.getCast().getName(),
                        mc.getCharacterName(),
                        mc.getCast().getProfilePath()
                ))
                .collect(Collectors.toList());

        Boolean hasImages = null;
        Boolean hasVideos = null;

        return MovieDetailDTO.builder()
                .tmdbId(id)
                .title(title)
                .overview(overview)
                .originalTitle(originalTitle)
                .originalLanguage(originalLanguage)
                .releaseDate(releaseDate)
                .runtime(runtime)
                .popularity(popularity)
                .voteAverage(voteAverage)
                .voteCount(voteCount)
                .status(status)
                .tagline(tagline)
                .budget(budget)
                .revenue(revenue)
                .homepage(homepage)
                .backdropPath(backdropPath)
                .posterPath(posterPath)
                .releasedInKorea(releasedInKorea)
                .releaseDateKorea(releaseDateKorea)
                .genres(genreDTOs)
                .casts(castList)
                .hasImages(hasImages)
                .hasVideos(hasVideos)
                .build();
    }
}
