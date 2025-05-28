package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MyPageDTO;
import org.dfpl.lecture.db.backend.dto.MyReviewDTO;
import org.dfpl.lecture.db.backend.dto.ReviewResponse;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.ReviewService;
import org.dfpl.lecture.db.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public UserController(ReviewService reviewService,
                          UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository     = userRepository;
    }

    @GetMapping("/me/reviews")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal
    ) {
        // 1) 토큰에서 꺼낸 email
        String email = principal.getUsername();

        // 2) DB에서 유저 엔티티 로드 (optional)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // 3) 서비스에 위임해서 나의 리뷰 리스트 조회
        List<ReviewResponse> reviews = reviewService.findByUser(user);

        return ResponseEntity.ok(reviews);
    }
}

