package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final UserService userService;

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

    private User getCurrentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + auth.getName()));
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<MyReviewDTO>> getMyReviews(Authentication auth) {
        User currentUser = getCurrentUser(auth);
        List<MyReviewDTO> reviews = userService.getMyReviews(currentUser);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MyPageDTO> myPage(Authentication auth) {
        User currentUser = getCurrentUser(auth);
        MyPageDTO dto = userService.getMyPage(currentUser);
        return ResponseEntity.ok(dto);
    }
}

