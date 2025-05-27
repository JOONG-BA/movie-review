package org.dfpl.lecture.db.backend.controller;

import org.dfpl.lecture.db.backend.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class UserController {

    @GetMapping("/api/users/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "nickname", user.getNickname()
        ));
    }
}
