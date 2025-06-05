package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.ReviewRequest;
import org.dfpl.lecture.db.backend.dto.ReviewResponse;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(
            @RequestBody ReviewRequest req,
            Authentication authentication          // Spring Security Authentication 객체
    ) {
        // 1) 토큰에서 꺼낸 사용자 이름(이메일)
        String email = authentication.getName();

        // 2) DB에서 JPA User 엔티티 조회
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 3) 서비스에 위임
        Long reviewId = reviewService.create(currentUser, req);

        return ResponseEntity.ok(Map.of(
                "message", "리뷰가 등록되었습니다.",
                "reviewId", reviewId
        ));
    }


    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal
    ) {
        String email = principal.getUsername();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        reviewService.delete(reviewId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByMovie(
            @PathVariable("movieId") Long movieId
    ) {
        List<ReviewResponse> reviews = reviewService.findByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

}
