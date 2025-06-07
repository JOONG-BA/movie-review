package org.dfpl.lecture.db.backend.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.ReviewRequest;
import org.dfpl.lecture.db.backend.dto.ReviewResponse;
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

    public List<ReviewResponse> findByUser(User user) {
        return reviewRepository.findAllByUser(user)
                .stream()
                .map(r -> new ReviewResponse(
                        r.getMovie().getTitle(),
                        // null-safe 처리 추가
                        r.getScore() != null ? r.getScore() : 0,
                        r.getContent(),
                        r.getUser().getNickname()
                ))
                .toList();
    }


    public Long create(User user, ReviewRequest request) {
        MovieDB movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다"));

        boolean hasScore = request.getScore() != null;
        boolean hasContent = request.getContent() != null && !request.getContent().isBlank();

        if (!hasScore && !hasContent) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "score 또는 content 중 하나는 반드시 입력해야 합니다.");
        }

        if (hasScore && (request.getScore() < 1 || request.getScore() > 5)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "score는 1~5 사이의 정수여야 합니다.");
        }

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .score(request.getScore())
                .content(request.getContent())
                .build();

        Review saved = reviewRepository.save(review);
        return saved.getId();
    }

    public void delete(Long reviewId, User currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다"));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다");
        }

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> findByMovieId(Long movieId) {
        // (선택 사항) movieId가 실제 존재하는 영화인지 확인하고 싶다면:
        MovieDB movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 영화가 없습니다. id=" + movieId
                ));

        // Repository에서 movie_id가 일치하는 모든 Review 엔티티 가져오기
        List<Review> reviews = reviewRepository.findAllByMovie_Id(movieId);

        // Review → ReviewResponse DTO 변환
        return reviews.stream()
                .map(r -> new ReviewResponse(
                        r.getMovie().getTitle(),
                        r.getScore(),
                        r.getContent(),
                        r.getUser().getNickname()
                ))
                .toList();
    }
}
