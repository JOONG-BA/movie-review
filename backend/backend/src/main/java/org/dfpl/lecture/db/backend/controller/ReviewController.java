package org.dfpl.lecture.db.backend.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.ReviewRequest;
import org.dfpl.lecture.db.backend.dto.ReviewResponse;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;


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

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> upsert(
            @RequestBody ReviewRequest req,
            Authentication authentication
    ) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Review saved = reviewService.upsertReview(user, req);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("reviewId", saved.getId());
        if (req.getScore() != null)   body.put("score",   saved.getScore());
        if (req.getContent() != null) body.put("content", saved.getContent());

        return ResponseEntity.ok(body);
    }
}
