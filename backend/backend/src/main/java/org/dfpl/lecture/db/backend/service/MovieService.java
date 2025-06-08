package org.dfpl.lecture.db.backend.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;
import org.dfpl.lecture.db.backend.dto.*;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.GenreMappingUtil;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final Gson gson = new Gson();
    private final MovieRepository movieRepository;

    // 한 페이지당 TMDB가 반환하는 영화 개수(고정 20개)
    private static final int PAGE_SIZE = 20;
    // TMDB Rate Limit 회피용 최소 딜레이(ms)
    private static final long CALL_DELAY_MS = 300L;

    /**
     * TMDB Discover API를 이용해서 1페이지부터 total_pages만큼 호출하여
     * 영화 정보를 모두 DB에 저장/업데이트합니다.
     */
    @Transactional
    public void loadAllAvailable() throws IOException, InterruptedException {
        System.out.println(">> loadAllAvailable() 진입, 첫 페이지 호출 준비");

        // 0) 첫 페이지를 호출해서 전체 페이지 수(total_pages)를 구함
        String firstEndpoint = "/discover/movie"
                + "?sort_by=popularity.desc"
                + "&language=ko-KR"
                + "&primary_release_date.gte=1990-01-01"
                + "&region=ko-KR"
                + "&vote_count.gte=500"
                + "&watch_region=ko-KR"
                + "&page=1";

        JsonObject firstRoot = fetchJson(firstEndpoint);
        int totalPages = firstRoot.get("total_pages").getAsInt();  // 예: 315

        // 1) 1페이지부터 totalPages까지 순회하면서 데이터를 가져오고 저장
        for (int page = 1; page <= totalPages; page++) {
            String endpoint = "/discover/movie"
                    + "?sort_by=popularity.desc"
                    + "&language=ko-KR"
                    + "&primary_release_date.gte=1990-01-01"
                    + "&region=ko-KR"
                    + "&vote_count.gte=500"
                    + "&watch_region=ko-KR"
                    + "&page=" + page;

            JsonObject root = fetchJson(endpoint);
            JsonArray results = root.getAsJsonArray("results");
            if (results == null || results.size() == 0) {
                break;
            }

            // 2) JSON 배열을 MovieSummaryDTO 리스트로 변환
            List<MovieSummaryDTO> dtoList = parseMovieSummaryList(results);

            // 3) DTO 리스트를 DB 저장/업데이트
            saveOrUpdate(dtoList);

            // 4) TMDB Rate Limit을 피하기 위해 잠시 Sleep
            Thread.sleep(CALL_DELAY_MS);
        }
    }

    /**
     * TMDB /discover/movie 결과(JsonArray)를 받아서 MovieSummaryDTO 리스트로 변환
     */
    private List<MovieSummaryDTO> parseMovieSummaryList(JsonArray results) {
        List<MovieSummaryDTO> list = new ArrayList<>();
        if (results == null) return list;

        for (JsonElement elem : results) {
            JsonObject obj = elem.getAsJsonObject();
            MovieSummaryDTO dto = new MovieSummaryDTO();

            dto.setId(obj.get("id").getAsLong());
            dto.setTitle(obj.get("title").getAsString());
            dto.setOriginalTitle(obj.get("original_title").getAsString());
            dto.setOverview(obj.get("overview").getAsString());
            dto.setPopularity(obj.get("popularity").getAsDouble());
            dto.setReleaseDate(obj.get("release_date").getAsString());
            dto.setVoteAverage(obj.get("vote_average").getAsDouble());
            dto.setVoteCount(obj.get("vote_count").getAsInt());

            // poster_path → full URL
            if (obj.has("poster_path") && !obj.get("poster_path").isJsonNull()) {
                String posterPath = obj.get("poster_path").getAsString();
                dto.setPosterUrl(TmdbApiUtil.getPosterImageUrl(posterPath));
            }
            // backdrop_path → full URL
            if (obj.has("backdrop_path") && !obj.get("backdrop_path").isJsonNull()) {
                String backdropPath = obj.get("backdrop_path").getAsString();
                dto.setBackdropUrl(TmdbApiUtil.getBackdropImageUrl(backdropPath));
            }

            // genre_ids → List<GenreDTO> 매핑 (GenreMappingUtil 사용)
            List<GenreDTO> genreList = new ArrayList<>();
            if (obj.has("genre_ids") && obj.get("genre_ids").isJsonArray()) {
                JsonArray ids = obj.getAsJsonArray("genre_ids");
                for (JsonElement idElem : ids) {
                    long gid = idElem.getAsLong();
                    String name = GenreMappingUtil.GENRE_ID_TO_NAME.get(gid);
                    if (name != null) {
                        genreList.add(new GenreDTO(gid, name));
                    }
                }
            }
            dto.setGenres(genreList);

            list.add(dto);
        }
        return list;
    }

    /**
     * 가져온 MovieSummaryDTO 리스트를 DB에 저장하거나 업데이트합니다.
     * - 기존에 저장된 영화면 필요한 필드만 업데이트
     * - 신규 영화면 insert
     * - DTO의 genres(GenreDTO 리스트)에서 ID만 꺼내 genre1~genre4에 하드코딩으로 분배
     */
    @Transactional
    public void saveOrUpdate(List<MovieSummaryDTO> movieSummaryDTOS) {
        for (MovieSummaryDTO dto : movieSummaryDTOS) {
            Long movieId = dto.getId();
            Optional<MovieDB> movieOpt = movieRepository.findById(movieId);

            // DTO의 GenreDTO 리스트에서 ID만 꺼내옴
            List<Long> incomingGenreIds = dto.getGenres() == null
                    ? Collections.emptyList()
                    : dto.getGenres().stream()
                    .map(GenreDTO::getId)
                    .toList();

            // 최대 4개까지만 사용 (없으면 null)
            Long g1 = incomingGenreIds.size() > 0 ? incomingGenreIds.get(0) : null;
            Long g2 = incomingGenreIds.size() > 1 ? incomingGenreIds.get(1) : null;
            Long g3 = incomingGenreIds.size() > 2 ? incomingGenreIds.get(2) : null;
            Long g4 = incomingGenreIds.size() > 3 ? incomingGenreIds.get(3) : null;

            if (movieOpt.isPresent()) {
                // ── (A) 기존 영화가 DB에 이미 있는 경우: 필요한 필드만 업데이트
                MovieDB existing = movieOpt.get();
                boolean needUpdate = false;

                // 1) 일반 필드 비교
                if (!Objects.equals(existing.getTitle(), dto.getTitle())) {
                    existing.setTitle(dto.getTitle());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getOriginalTitle(), dto.getOriginalTitle())) {
                    existing.setOriginalTitle(dto.getOriginalTitle());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getOverview(), dto.getOverview())) {
                    existing.setOverview(dto.getOverview());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getPopularity(), dto.getPopularity())) {
                    existing.setPopularity(dto.getPopularity());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getReleaseDate(), dto.getReleaseDate())) {
                    existing.setReleaseDate(dto.getReleaseDate());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getVoteAverage(), dto.getVoteAverage())) {
                    existing.setVoteAverage(dto.getVoteAverage());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getVoteCount(), dto.getVoteCount())) {
                    existing.setVoteCount(dto.getVoteCount());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getPosterUrl(), dto.getPosterUrl())) {
                    existing.setPosterUrl(dto.getPosterUrl());
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getBackdropUrl(), dto.getBackdropUrl())) {
                    existing.setBackdropUrl(dto.getBackdropUrl());
                    needUpdate = true;
                }

                // 2) 하드코딩 장르(genre1~4) 비교 및 설정
                if (!Objects.equals(existing.getGenre1(), g1)) {
                    existing.setGenre1(g1);
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getGenre2(), g2)) {
                    existing.setGenre2(g2);
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getGenre3(), g3)) {
                    existing.setGenre3(g3);
                    needUpdate = true;
                }
                if (!Objects.equals(existing.getGenre4(), g4)) {
                    existing.setGenre4(g4);
                    needUpdate = true;
                }

                // 3) 변경이 발생한 경우에만 save 호출
                if (needUpdate) {
                    movieRepository.save(existing);
                }

            } else {
                // ── (B) 신규 영화인 경우: insert
                MovieDB movie = MovieDB.builder()
                        .id(dto.getId())
                        .title(dto.getTitle())
                        .originalTitle(dto.getOriginalTitle())
                        .overview(dto.getOverview())
                        .popularity(dto.getPopularity())
                        .voteAverage(dto.getVoteAverage())
                        .voteCount(dto.getVoteCount())
                        .releaseDate(dto.getReleaseDate())
                        .posterUrl(dto.getPosterUrl())
                        .backdropUrl(dto.getBackdropUrl())
                        .genre1(g1)
                        .genre2(g2)
                        .genre3(g3)
                        .genre4(g4)
                        .build();

                movieRepository.save(movie);
            }
        }
    }


    private SearchResultDTO toSearchResultDto(MovieDB entity) {
        Double rawVote = entity.getVoteAverage();
        double halfVote = (rawVote != null ? rawVote / 2.0 : 0.0);
        return new SearchResultDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getReleaseDate(),
                entity.getPosterUrl(),
                halfVote
        );
    }

    /**
     * 1) DB에서 “제목 or 개요 LIKE” 검색 → DTO 페이지로 리턴
     */
    @Transactional(readOnly = true)
    public Page<SearchResultDTO> searchMoviesInDb(
            String keyword, int page, int size
    ) {
        // Pageable에 인기순 정렬을 붙여서 생성
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("popularity").descending());
        // Repository의 custom query 호출
        Page<MovieDB> entityPage = movieRepository.searchByKeywordOrderByPopularityDesc(keyword, pageable);
        // 엔티티 페이지 → DTO 페이지로 매핑
        return entityPage.map(this::toSearchResultDto);
    }

    /**
     * 2) DB에 저장된 전체 영화 중 인기순 페이징 조회 → DTO 페이지로 리턴
     */
    @Transactional(readOnly = true)
    public Page<SearchResultDTO> getPopularFromDb(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("popularity").descending());
        Page<MovieDB> entityPage = movieRepository.findAllByOrderByPopularityDesc(pageable);
        return entityPage.map(this::toSearchResultDto);
    }

    /**
     * 3) DB에서 특정 장르별로 인기순 페이징 조회 → DTO 페이지로 리턴
     */
    @Transactional(readOnly = true)
    public Page<SearchResultDTO> getPopularByGenreFromDb(
            Long genreId, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        // 여기가 조인 버전으로 바뀐 메서드를 호출하게 됩니다
        Page<MovieDB> entityPage = movieRepository.findByGenreIdOrderByPopularityDesc(genreId, pageable);
        return entityPage.map(this::toSearchResultDto);
    }





    /**
     * TMDB API를 호출하고 JSON 본문을 JsonObject로 파싱하여 반환하는 헬퍼 메서드
     */
    private JsonObject fetchJson(String endpointWithQuery) throws IOException {
        Request request = TmdbApiUtil.buildRequest(endpointWithQuery);
        try (Response response = TmdbApiUtil.getClient().newCall(request).execute()) {
            String body = response.body().string();
            return JsonParser.parseString(body).getAsJsonObject();
        }
    }

    /**
     * TMDB에서 영화 세부 정보를 조회하여 MovieDetailDTO로 반환합니다.
     * 저장하지 않고, 바로 TMDB에서 읽어서 DTO만 만듭니다.
     */
    public MovieDetailDTO getMovieDetail (Long movieId) throws IOException {
        MovieDetailDTO dto = new MovieDetailDTO();
        dto.setId(movieId);

        // 1) 기본 정보 (/movie/{id}?language=ko-KR)
        JsonObject movieObj = fetchJson(TmdbApiUtil.withLanguage("/movie/" + movieId));
        dto.setTitle(movieObj.get("title").getAsString());
        dto.setOriginalTitle(movieObj.get("original_title").getAsString());
        dto.setOverview(movieObj.get("overview").getAsString());

        // release_date 예: "2025-06-03" → 연도만 잘라서 사용
        String releaseDate = movieObj.get("release_date").getAsString();
        dto.setReleaseYear(releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : releaseDate);


        String posterPath = null;
        if (movieObj.has("poster_path") && !movieObj.get("poster_path").isJsonNull()) {
            posterPath = movieObj.get("poster_path").getAsString();
        }

        String backdropPath = null;
        if (movieObj.has("backdrop_path") && !movieObj.get("backdrop_path").isJsonNull()) {
            backdropPath = movieObj.get("backdrop_path").getAsString();
        }

        dto.setPosterUrl(TmdbApiUtil.getPosterImageUrl(posterPath));
        dto.setBackdropUrl(TmdbApiUtil.getBackdropImageUrl(backdropPath));

        // production_countries 배열 중 첫 번째 항목 name
        JsonArray countries = movieObj.getAsJsonArray("production_countries");
        if (countries != null && countries.size() > 0) {
            dto.setCountry(countries.get(0).getAsJsonObject().get("name").getAsString());
        }

        // runtime (Integer)
        if (!movieObj.get("runtime").isJsonNull()) {
            dto.setRuntime(movieObj.get("runtime").getAsInt());
        }

        // TMDB 평점 + 투표 수
        dto.setVoteAverage(movieObj.get("vote_average").getAsDouble() / 2.0);
        dto.setVoteCount(movieObj.get("vote_count").getAsInt());

        // 장르 리스트 (genres 배열에서 name만 추출)
        JsonArray genresArr = movieObj.getAsJsonArray("genres");
        List<String> genreList = new ArrayList<>();
        if (genresArr != null) {
            for (JsonElement g : genresArr) {
                genreList.add(g.getAsJsonObject().get("name").getAsString());
            }
        }
        dto.setGenres(genreList);

        // 2) 출연진 & 감독 (/movie/{id}/credits?language=ko-KR)
        JsonObject creditsObj = fetchJson(TmdbApiUtil.withLanguage("/movie/" + movieId + "/credits"));
        JsonArray castArr = creditsObj.getAsJsonArray("cast");
        JsonArray crewArr = creditsObj.getAsJsonArray("crew");

        // – 배우 리스트
        List<CastDTO> castList = new ArrayList<>();
        if (castArr != null) {
            int limit = Math.min(castArr.size(), 10);
            for (int i = 0; i < limit; i++) {
                JsonObject castItem = castArr.get(i).getAsJsonObject();
                CastDTO c = new CastDTO();
                c.setName(castItem.get("name").getAsString());
                c.setCharacter(castItem.get("character").getAsString());

                JsonElement profilePathElem = castItem.get("profile_path");
                if (!profilePathElem.isJsonNull()) {
                    String path = profilePathElem.getAsString();
                    c.setProfileImageUrl(TmdbApiUtil.getCreditsImageUrl(path));
                }
                castList.add(c);
            }
        }
        dto.setCast(castList);

        // – 감독 (crew 배열 중 job=="Director"인 요소)
        if (crewArr != null) {
            for (JsonElement e : crewArr) {
                JsonObject crewItem = e.getAsJsonObject();
                if ("Director".equals(crewItem.get("job").getAsString())) {
                    PersonDTO directorDto = new PersonDTO();
                    directorDto.setName(crewItem.get("name").getAsString());

                    JsonElement dirProfilePath = crewItem.get("profile_path");
                    if (!dirProfilePath.isJsonNull()) {
                        directorDto.setProfileImageUrl(TmdbApiUtil.getCreditsImageUrl(dirProfilePath.getAsString()));
                    }
                    dto.setDirector(directorDto);
                    break;
                }
            }
        }

        // 3) 이미지 (갤러리) (/movie/{id}/images)
        JsonObject imagesObj = fetchJson("/movie/" + movieId + "/images?include_image_language=null");
        List<ImageDTO> gallery = new ArrayList<>();

        JsonArray backdrops = imagesObj.getAsJsonArray("backdrops");

        if (backdrops != null) {
            // JsonArray backdrops의 크기만큼 반복하되, 인덱스가 10을 넘지 않도록 조건 추가
            for (int i = 0; i < backdrops.size() && i < 10; i++) {
                JsonObject imgObj = backdrops.get(i).getAsJsonObject();
                String filePath = imgObj.get("file_path").getAsString();

                ImageDTO imgDto = new ImageDTO();
                imgDto.setUrl(TmdbApiUtil.getImageUrl(filePath));
                gallery.add(imgDto);
            }
        }
        dto.setGalleryImages(gallery);


        // 4) 예고편/트레일러 (/movie/{id}/videos?language=ko-KR)
        JsonObject videosObj = fetchJson(TmdbApiUtil.withLanguage("/movie/" + movieId + "/videos"));
        List<VideoDTO> videoList = new ArrayList<>();

        JsonArray videoArr = videosObj.getAsJsonArray("results");
        if (videoArr != null) {
            for (JsonElement e : videoArr) {
                JsonObject vidObj = e.getAsJsonObject();
                if ("YouTube".equals(vidObj.get("site").getAsString())) {
                    String type = vidObj.get("type").getAsString();
                    if ("Trailer".equals(type) || "Teaser".equals(type)) {
                        VideoDTO vDto = new VideoDTO();
                        vDto.setName(vidObj.get("name").getAsString());
                        String key = vidObj.get("key").getAsString();
                        vDto.setUrl("https://www.youtube.com/watch?v=" + key);
                        videoList.add(vDto);
                    }
                }
            }
        }
        dto.setTrailers(videoList);

        return dto;
    }
}

