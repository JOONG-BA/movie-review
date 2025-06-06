package org.dfpl.lecture.db.backend.service;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;
import org.dfpl.lecture.db.backend.dto.*;
import org.dfpl.lecture.db.backend.entity.Genre;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.repository.GenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.GenreMappingUtil;
import org.dfpl.lecture.db.backend.util.TmdbApiUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final Gson gson = new Gson();

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;



    /**
     * 간단히 endpointWithQuery를 받아 요청을 보내고
     * JSON 본문을 JsonObject로 파싱하여 반환하는 헬퍼 메서드입니다.
     */
    private JsonObject fetchJson(String endpointWithQuery) throws IOException {
        Request request = TmdbApiUtil.buildRequest(endpointWithQuery);
        try (Response response = TmdbApiUtil.getClient().newCall(request).execute()) {
            String body = response.body().string();
            return JsonParser.parseString(body).getAsJsonObject();
        }
    }

    /**
     * 장르별 인기순 조회 예시:
     * PageRequest.of(page, size, Sort.by("voteCount").descending()) 같이 Pageable로 넘겨주면 됩니다.
     */

    // 한 페이지당 TMDB가 반환하는 영화 개수(고정 20개)
    private static final int PAGE_SIZE = 20;
    // TMDB Rate Limit 회피용 최소 딜레이(ms)
    private static final long CALL_DELAY_MS = 300L;

    /**
     * Discover API를 1페이지부터 total_pages(315)까지 순차 호출하여,
     * 전체(≈6,290개) 영화를 DB에 저장/업데이트합니다.
     */
    @Transactional
    public void loadAllAvailable() throws IOException, InterruptedException {
        System.out.println(">> loadAllAvailable() 진입, 첫 페이지 호출 준비");
        // 1) 첫 페이지를 호출해서 total_pages 값을 얻음
        String firstEndpoint = "/discover/movie"
                + "?sort_by=popularity.desc"
                + "&language=ko-KR"
                + "primary_release_date.gte=1990-01-01"
                + "region=ko-KR"
                + "vote_count.gte=500"
                + "watch_region=ko-KR"
                + "&page=" + 1;

        JsonObject firstRoot = fetchJson(firstEndpoint);
        int totalPages = firstRoot.get("total_pages").getAsInt();  // 315
        // totalResults = firstRoot.get("total_results").getAsInt(); // 6290

        // 2) 1페이지부터 totalPages까지 순차 호출
        for (int page = 1; page <= totalPages; page++) {
            String endpoint = "/discover/movie"
                    + "?sort_by=popularity.desc"
                    + "&language=ko-KR"
                    + "&page=" + page;

            JsonObject root = fetchJson(endpoint);
            JsonArray results = root.getAsJsonArray("results");
            if (results == null || results.size() == 0) {
                break;  // 만약 중간에 데이터가 비어 있으면 종료
            }

            // DTO 파싱 → DB 저장
            List<MovieSummaryDTO> dtoList = parseMovieSummaryList(results);
            saveOrUpdate(dtoList);

            Thread.sleep(CALL_DELAY_MS);
        }
        // 총 약 6,290개 영화를 저장 (315페이지 × 20개 = 6,300개 중 일부 페이지에는 20개 미만인 경우가 있어 최종 약 6,290개)
    }



    /**
     * results 배열(JsonArray)을 받아서 MovieSummaryDTO 리스트로 변환하는 헬퍼
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

            // poster_path → 풀 URL
            if (obj.has("poster_path") && !obj.get("poster_path").isJsonNull()) {
                String posterPath = obj.get("poster_path").getAsString();
                dto.setPosterUrl(TmdbApiUtil.getPosterImageUrl(posterPath));
            }
            // backdrop_path → 풀 URL (필요 시)
            if (obj.has("backdrop_path") && !obj.get("backdrop_path").isJsonNull()) {
                String backdropPath = obj.get("backdrop_path").getAsString();
                dto.setBackdropUrl(TmdbApiUtil.getBackdropImageUrl(backdropPath));
            }

            // genre_ids → List<GenreDTO> 매핑 (앞서 정의한 유틸 사용)
            List<GenreDTO> genreList = new ArrayList<>();
            if (obj.has("genre_ids") && obj.get("genre_ids").isJsonArray()) {
                JsonArray ids = obj.getAsJsonArray("genre_ids");
                for (JsonElement idElem : ids) {
                    int gid = idElem.getAsInt();
                    String name = GenreMappingUtil.GENRE_ID_TO_NAME.get(gid);
                    if (name != null) {
                        genreList.add(new GenreDTO((long)gid, name));
                    }
                }
            }
            dto.setGenres(genreList);

            list.add(dto);
        }
        return list;
    }

    private void saveOrUpdate(List<MovieSummaryDTO> movieSummaryDTOS) {
        for (MovieSummaryDTO dto : movieSummaryDTOS) {
            Long id = dto.getId();
            Optional<MovieDB> movieDB = movieRepository.findById(id);

            if (movieDB.isPresent()) {
                MovieDB exisiting =  movieDB.get();
                boolean needUpdate = false;

                if(!Objects.equals(exisiting.getTitle(), dto.getTitle())) {
                    exisiting.setTitle(dto.getTitle());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getOriginalTitle(), dto.getOriginalTitle())) {
                    exisiting.setOriginalTitle(dto.getOriginalTitle());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getOverview(), dto.getOverview())) {
                    exisiting.setOverview(dto.getOverview());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getPopularity(), dto.getPopularity())) {
                    exisiting.setPopularity(dto.getPopularity());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getReleaseDate(), dto.getReleaseDate())) {
                    exisiting.setReleaseDate(dto.getReleaseDate());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getVoteAverage(), dto.getVoteAverage())) {
                    exisiting.setVoteAverage(dto.getVoteAverage());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getVoteCount(), dto.getVoteCount())) {
                    exisiting.setVoteCount(dto.getVoteCount());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getPosterUrl(), dto.getPosterUrl())) {
                    exisiting.setPosterUrl(dto.getPosterUrl());
                    needUpdate = true;
                }
                if(!Objects.equals(exisiting.getBackdropUrl(), dto.getBackdropUrl())) {
                    exisiting.setBackdropUrl(dto.getBackdropUrl());
                    needUpdate = true;
                }
            }
            else{
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
                        .build();
                if(dto.getGenres() != null && !dto.getGenres().isEmpty()) {
                    List<Genre> genreEntities = new ArrayList<>();
                    for(GenreDTO genreDTO : dto.getGenres()) {
                        Genre genre = genreRepository.findById(genreDTO.getId())
                                .orElseGet(()->{
                                    Genre g = new Genre();
                                    g.setId(genreDTO.getId());
                                    g.setName(genreDTO.getName());
                                    return genreRepository.save(g);
                                });
                            genreEntities.add(genre);
                    }
                    movie.setGenres(genreEntities);
                }

                movieRepository.save(movie);
            }
        }
    }

    /**
     * TMDB /search/movie API 호출 → 결과를 SearchResultDTO 리스트로 반환
     *
     * @param query 검색어 (예: "인셉션")
     * @param page  TMDB 검색 페이지 (1부터 시작); null일 경우 기본 1 사용
     */
    public List<MovieSummaryDTO> searchMovies(String query, int page) throws IOException {
        // 1) 검색어 URL 인코딩
        String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

        // 2) /search/movie 엔드포인트 구성 (language=ko-KR 포함)
        String endpoint = "/search/movie"
                + "?query=" + encoded
                + "&page=" + page
                + "&language=ko-KR";

        // 3) TMDB 호출
        JsonObject root = fetchJson(endpoint);
        JsonArray results = root.getAsJsonArray("results");

        List<MovieSummaryDTO> list = new ArrayList<>();
        if (results != null) {
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

                // backdrop_path → full URL (필요 시)
                if (obj.has("backdrop_path") && !obj.get("backdrop_path").isJsonNull()) {
                    String backdropPath = obj.get("backdrop_path").getAsString();
                    dto.setBackdropUrl(TmdbApiUtil.getBackdropImageUrl(backdropPath));
                }

                // 4) genre_ids 배열 → List<GenreDTO> 매핑
                List<GenreDTO> genreList = new ArrayList<>();
                if (obj.has("genre_ids") && obj.get("genre_ids").isJsonArray()) {
                    JsonArray genreIdsArr = obj.getAsJsonArray("genre_ids");
                    for (JsonElement idElem : genreIdsArr) {
                        int genreId = idElem.getAsInt();
                        String name = GenreMappingUtil.GENRE_ID_TO_NAME.get(genreId);
                        if (name != null) {
                            genreList.add(new GenreDTO((long)genreId, name));
                        }
                    }
                }
                dto.setGenres(genreList);

                list.add(dto);
            }
        }

        return list;
    }

    /**
     * TMDB에서 영화 세부 정보를 조회하여 MovieDetailDTO로 반환합니다.
     * 저장하지 않고, 바로 TMDB에서 읽어서 DTO만 만듭니다.
     */
    public MovieDetailDTO getMovieDetail(Long movieId) throws IOException {
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
        dto.setVoteAverage(movieObj.get("vote_average").getAsDouble());
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
