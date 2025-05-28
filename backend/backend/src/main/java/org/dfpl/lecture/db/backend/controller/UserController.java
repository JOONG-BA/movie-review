package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.MyPageDTO;
import org.dfpl.lecture.db.backend.dto.MyReviewDTO;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public MyPageDTO mypage(@AuthenticationPrincipal User user) {
        return userService.getMyPage(user);
    }

    @GetMapping("/me/reviews")
    @PreAuthorize("hasRole('USER')")
    public List<MyReviewDTO> myReviews(@AuthenticationPrincipal User user) {
        return userService.getMyReviews(user);
    }
}
