package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.ReviewRequest;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;

import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public Long create(User user, ReviewRequest request) {
        MovieDB movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다"));

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .score(request.getScore())
                .content(request.getContent())
                .build();

        Review saved = reviewRepository.save(review);
        return saved.getId();
    }

    public void delete(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다");
        }

        reviewRepository.delete(review);
    }
}
