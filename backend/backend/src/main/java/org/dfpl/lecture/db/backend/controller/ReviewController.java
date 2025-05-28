package org.dfpl.lecture.db.backend.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.ReviewRequest;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
            @AuthenticationPrincipal User user
    ) {
        Long id = reviewService.create(user, req);
        return ResponseEntity.ok(Map.of(
                "message", "리뷰가 등록되었습니다.",
                "reviewId", id
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

}
