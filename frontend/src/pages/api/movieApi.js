import axios from 'axios';

const API_BASE = '/api/movies';

/**
 * 영화 상세 정보 조회
 * @param {number} id
 * @returns {Promise<MovieDetailDTO>}
 */
export const getMovieDetail = async (id) => {
    const res = await axios.get(`${API_BASE}/detail/${id}`);
    return res.data;
};

/**
 * @param {string} query - 검색어
 * @param {number} page - 페이지 번호 (기본값: 1)
 * @returns {Promise<SearchResultDTO[]>}
 */
export const searchMovies = async (query, page = 1) => {
    const res = await axios.get(`${API_BASE}/search`, {
        params: { query, page },
    });
    return res.data;
};

/**
 * DB 저장 영화 인기순 조회
 * @param {number} page - 0부터 시작
 * @param {number} size - 페이지 크기
 * @returns {Promise<Page<MovieDB>>}
 */
export const getAllPopular = async (page = 0, size = 10) => {
    const res = await axios.get(`${API_BASE}/popular`, {
        params: { page, size },
    });
    return res.data;
};

/**
 * @param {number} page
 * @returns {Promise<SearchResultDTO[]>}
 */
export const getPopularFromApi = async (page = 1) => {
    const res = await axios.get(`${API_BASE}/popular/api`, {
        params: { page },
    });
    return res.data;
};

/**
 * 인기 장르별 영화 조회
 * @param {number} genreId -
 * @param {number} page
 * @returns {Promise<SearchResultDTO[]>}
 */
export const getPopularByGenreFromApi = async (genreId, page = 1) => {
    const res = await axios.get(`${API_BASE}/popular/genre/api`, {
        params: { genre: genreId, page },
    });
    return res.data;
};
