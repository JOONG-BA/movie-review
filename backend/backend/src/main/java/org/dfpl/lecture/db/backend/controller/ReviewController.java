package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.dfpl.lecture.db.backend.dto.ReviewDTO;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.repository.ReviewRepository;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public ReviewController(ReviewRepository reviewRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest req,
                                          @AuthenticationPrincipal User user) {
        MovieDB movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다"));

        Review saved = reviewRepository.save(Review.builder()
                .movie(movie)
                .user(user)
                .score(req.getScore())
                .content(req.getContent())
                .build());

        return ResponseEntity.ok(Map.of(
                "message", "리뷰가 등록되었습니다",
                "reviewId", saved.getId()
        ));
    }

    @Data
    public static class ReviewRequest {
        private Long movieId;
        private int score;
        private String content;
    }
}
