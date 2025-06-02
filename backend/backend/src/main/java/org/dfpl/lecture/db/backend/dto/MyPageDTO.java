package org.dfpl.lecture.db.backend.dto;

import java.util.List;
import lombok.Data;

@Data
public class MyPageDTO {
    private Long id;
    private String email;
    private String nickname;
    private long reviewCount;
    private long favoriteCount;
    private List<MyReviewDTO> recentReviews;    // 최신 3개
    private List<FavoriteMovieDTO> favoriteMovies; // 최신 3개 즐겨찾기 영화
}
