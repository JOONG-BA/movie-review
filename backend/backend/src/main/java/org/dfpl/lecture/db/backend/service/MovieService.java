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
     * TMDB에서 영화 세부정보를 호출하여 MovieDetailDTO를 만든 뒤,
     * 로컬 DB(MovieDB)에도 저장하는 예제 메서드입니다.
     */
    @Transactional
    public MovieDetailDTO fetchAndSaveMovie(Long movieId) throws IOException {
        // 1) TMDB에서 기본 영화 정보 JSON 가져오기
        JsonObject movieObj = fetchJson(TmdbApiUtil.withLanguage("/movie/" + movieId));

        // 2) MovieDetailDTO 조립
        MovieDetailDTO dto = new MovieDetailDTO();
        dto.setId(movieId);
        dto.setTitle(movieObj.get("title").getAsString());
        dto.setOriginalTitle(movieObj.get("original_title").getAsString());
        dto.setOverview(movieObj.get("overview").getAsString());

        String releaseDate = movieObj.get("release_date").getAsString();
        dto.setReleaseYear(releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : releaseDate);

        JsonArray countries = movieObj.getAsJsonArray("production_countries");
        dto.setCountry((countries != null && countries.size() > 0)
                ? countries.get(0).getAsJsonObject().get("name").getAsString()
                : null);

        dto.setRuntime(movieObj.get("runtime").isJsonNull() ? null : movieObj.get("runtime").getAsInt());
        dto.setVoteAverage(movieObj.get("vote_average").getAsDouble());
        dto.setVoteCount(movieObj.get("vote_count").getAsInt());

        JsonArray genresArr = movieObj.getAsJsonArray("genres");
        List<String> genreNames = new ArrayList<>();
        for (JsonElement e : genresArr) {
            genreNames.add(e.getAsJsonObject().get("name").getAsString());
        }
        dto.setGenres(genreNames);

        // (출연진, 이미지, 비디오 파싱은 생략하고, 앞서 작성된 예제대로 넣으면 됩니다.)
        // dto.setCast(...);
        // dto.setDirector(...);
        // dto.setGalleryImages(...);
        // dto.setTrailers(...);

        // 3) MovieDB 엔티티 생성 및 저장 (장르 매핑 포함)
        MovieDB entity = MovieDB.builder()
                .id(movieId)
                .title(dto.getTitle())
                .originalTitle(dto.getOriginalTitle())
                .overview(dto.getOverview())
                // releaseDate를 LocalDate로 변환 (releaseYear만 있으면 임의로 1월 1일 설정)
                .releaseDate(LocalDate.parse(dto.getReleaseYear() + "-01-01"))
                .country(dto.getCountry())
                .runtime(dto.getRuntime())
                .voteAverage(dto.getVoteAverage())
                .voteCount(dto.getVoteCount())
                .posterPath(extractFirstPosterPath(movieId))
                .backdropPath(extractFirstBackdropPath(movieId))
                .build();

        // 3-1) TMDB JSON의 genres 배열을 순회하며, Genre 엔티티를 DB에서 찾아서 리스트로 준비
        List<Genre> genreEntities = new ArrayList<>();
        for (JsonElement ge : genresArr) {
            JsonObject gObj = ge.getAsJsonObject();
            Long genreId = gObj.get("id").getAsLong();
            String genreName = gObj.get("name").getAsString();

            // DB에 이미 있으면 가져오고, 없으면 새로 저장
            Genre genre = genreRepository.findById(genreId)
                    .orElseGet(() -> {
                        Genre newGenre = Genre.builder()
                                .id(genreId)
                                .name(genreName)
                                .build();
                        return genreRepository.save(newGenre);
                    });
            genreEntities.add(genre);
        }
        // 3-2) MovieDB 엔티티에 장르 리스트 세팅 후 저장
        entity.setGenres(genreEntities);
        movieRepository.save(entity);

        return dto;
    }

    /**
     * /movie/{id}/images 호출 후, posters 배열 중 첫 번째 요소의 file_path 반환
     * (포스터 경로 없으면 null 반환)
     */
    private String extractFirstPosterPath(Long movieId) throws IOException {
        JsonObject imagesObj = fetchJson("/movie/" + movieId + "/images");
        JsonArray posters = imagesObj.getAsJsonArray("posters");
        if (posters != null && posters.size() > 0) {
            return posters.get(0).getAsJsonObject().get("file_path").getAsString();
        }
        return null;
    }

    /**
     * /movie/{id}/images 호출 후, backdrops 배열 중 첫 번째 요소의 file_path 반환
     * (백드롭 경로 없으면 null 반환)
     */
    private String extractFirstBackdropPath(Long movieId) throws IOException {
        JsonObject imagesObj = fetchJson("/movie/" + movieId + "/images");
        JsonArray backdrops = imagesObj.getAsJsonArray("backdrops");
        if (backdrops != null && backdrops.size() > 0) {
            return backdrops.get(0).getAsJsonObject().get("file_path").getAsString();
        }
        return null;
    }

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
    public List<MovieSummaryDTO> getPopularMovies(int page) throws IOException {
        // 엔드포인트 구성
        String endpoint = "/discover/movie"
                + "?sort_by=popularity.desc"
                + "&language=ko-KR"
                + "&page=" + page;

        JsonObject root = fetchJson(endpoint);
        return parseMovieSummaryList(root.getAsJsonArray("results"));
    }

    /**
     * (2) 장르별 인기순 목록을 가져온다.
     *     /discover/movie?with_genres={genreId}&sort_by=popularity.desc&language=ko-KR&page={page}
     */
    public List<MovieSummaryDTO> getPopularByGenre(int genreId, int page) throws IOException {
        String endpoint = "/discover/movie"
                + "?with_genres=" + genreId
                + "&sort_by=popularity.desc"
                + "&language=ko-KR"
                + "&page=" + page;

        JsonObject root = fetchJson(endpoint);
        return parseMovieSummaryList(root.getAsJsonArray("results"));
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
                            genreList.add(new GenreDTO(genreId, name));
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
    /**
     * DB에 저장된 모든 영화를 voteCount(투표 수) 기준 내림차순(인기순)으로 페이징 조회합니다.
     *
     * @param page  요청 페이지 번호 (0부터 시작)
     * @param size  페이지 크기 (한 페이지당 반환할 영화 수)
     * @return      voteCount 기준 내림차순으로 정렬된 Page<MovieDB>
     */
    public Page<MovieDB> findAllPopular(int page, int size) {
        // PageRequest.of(page, size, Sort.by("voteCount").descending())로 정렬
        return movieRepository.findAll(
                PageRequest.of(page, size, Sort.by("voteCount").descending())
        );
    }
    /**
     * TMDB API에서 인기 영화 리스트를 호출하여 SearchResultDTO 리스트로 반환합니다.
     *
     * @param page TMDB 인기 영화 페이지 (1부터 시작; null 또는 1 미만일 경우 자동으로 1 처리)
     * @return 인기 영화 목록 (한 페이지당 최대 20개)
     * @throws IOException 네트워크/파싱 예외 발생 시
     */
    public List<SearchResultDTO> getPopularFromApi(Integer page) throws IOException {
        if (page == null || page < 1) {
            page = 1;
        }

        // 1) TMDB 인기 영화 엔드포인트: /movie/popular
        //    언어: 한국어(ko-KR), 지역: KR(한국)
        String endpoint = TmdbApiUtil.withLanguageAndRegion("/movie/popular") + "&page=" + page;
        // 예: "/movie/popular?language=ko-KR&region=KR&page=1"

        Request request = TmdbApiUtil.buildRequest(endpoint);
        try (Response response = TmdbApiUtil.getClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // 4xx/5xx 응답이 왔을 때 예외 처리
                throw new IOException("TMDB 인기 영화 API 호출 실패: status=" + response.code());
            }

            String body = response.body().string();
            JsonObject root = JsonParser.parseString(body).getAsJsonObject();
            JsonArray results = root.getAsJsonArray("results");

            List<SearchResultDTO> list = new ArrayList<>();
            if (results != null) {
                for (JsonElement elem : results) {
                    JsonObject obj = elem.getAsJsonObject();
                    Long id = obj.get("id").getAsLong();
                    String title = obj.get("title").getAsString();
                    String releaseDate = obj.has("release_date") && !obj.get("release_date").isJsonNull()
                            ? obj.get("release_date").getAsString()
                            : null;
                    String posterPath = obj.has("poster_path") && !obj.get("poster_path").isJsonNull()
                            ? obj.get("poster_path").getAsString()
                            : null;
                    Double voteAverage = obj.has("vote_average") && !obj.get("vote_average").isJsonNull()
                            ? obj.get("vote_average").getAsDouble()
                            : null;

                    list.add(new SearchResultDTO(id, title, releaseDate, posterPath, voteAverage));
                }
            }
            return list;
        }
    }
    /**
     * TMDB API를 이용해 특정 장르(genreId) 기준으로 인기순(인기도 내림차순) 영화 목록을 가져옵니다.
     *
     * @param genreId TMDB 장르 ID (예: 28=액션, 12=어드벤처, 35=코미디 등)
     * @param page    결과 페이지 번호 (1부터 시작; null 또는 1 미만일 경우 1 처리)
     * @return        SearchResultDTO 리스트 (한 페이지 당 최대 20개)
     * @throws IOException 호출 또는 JSON 파싱 실패 시
     */
    public List<SearchResultDTO> getPopularByGenreFromApi(Long genreId, Integer page) throws IOException {
        if (page == null || page < 1) {
            page = 1;
        }
        if (genreId == null) {
            throw new IllegalArgumentException("genreId는 필수입니다.");
        }

        // 1) Discover API 엔드포인트 조립
        //    - with_genres=genreId
        //    - sort_by=popularity.desc (인기도 기준 내림차순)
        //    - language=ko-KR
        //    - page={page}
        String endpoint = "/discover/movie"
                + "?with_genres=" + genreId
                + "&sort_by=popularity.desc"
                + "&language=ko-KR"
                + "&page=" + page;

        Request request = TmdbApiUtil.buildRequest(endpoint);
        try (Response response = TmdbApiUtil.getClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("TMDB 장르별 인기 API 호출 실패: status=" + response.code());
            }
            String body = response.body().string();
            JsonObject root = JsonParser.parseString(body).getAsJsonObject();
            JsonArray results = root.getAsJsonArray("results");

            List<SearchResultDTO> list = new ArrayList<>();
            if (results != null) {
                for (JsonElement elem : results) {
                    JsonObject obj = elem.getAsJsonObject();
                    Long id = obj.get("id").getAsLong();
                    String title = obj.get("title").getAsString();
                    String releaseDate = obj.has("release_date") && !obj.get("release_date").isJsonNull()
                            ? obj.get("release_date").getAsString()
                            : null;
                    String posterPath = obj.has("poster_path") && !obj.get("poster_path").isJsonNull()
                            ? obj.get("poster_path").getAsString()
                            : null;
                    Double voteAverage = obj.has("vote_average") && !obj.get("vote_average").isJsonNull()
                            ? obj.get("vote_average").getAsDouble()
                            : null;

                    list.add(new SearchResultDTO(id, title, releaseDate, posterPath, voteAverage));
                }
            }
            return list;
        }
    }
}
