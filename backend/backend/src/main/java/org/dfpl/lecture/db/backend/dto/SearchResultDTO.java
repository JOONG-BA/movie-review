package org.dfpl.lecture.db.backend.dto;

import lombok.Data;

/**
 * /search/movie 결과 중, 검색 리스트 화면에서 보여줄 최소한의 필드만 담은 DTO
 */
@Data
public class SearchResultDTO {
    private Long id;              // TMDB 영화 ID
    private String title;         // 영화 제목
    private String releaseDate;   // 개봉일(문자열)
    private String posterPath;    // 포스터 파일 경로 (file_path 형태)
    private Double voteAverage;   // TMDB 평점

    public SearchResultDTO(Long id, String title, String releaseDate, String posterPath, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }
}
