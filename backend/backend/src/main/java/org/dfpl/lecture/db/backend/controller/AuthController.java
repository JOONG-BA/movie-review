package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import java.util.Map;
import javax.security.sasl.AuthenticationException;
import org.dfpl.lecture.db.backend.dto.*;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.AuthService;
import org.dfpl.lecture.db.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    record Message(String message) {}
    record Error(String error)   {}

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            authService.signup(request);
            return ResponseEntity.ok().body(new Message("회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1) 인증 시도 (이메일이 없거나, 비번 불일치하면 BadCredentialsException 발생)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            // 2) 인증 실패 시 JSON 에러 바디와 함께 401 리턴
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Error("이메일 또는 비밀번호가 일치하지 않습니다."));
        }

        // 3) 인증에 성공했어도, 혹시 이메일 조회가 실패하면 동일한 메시지를 던져줍니다.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "이메일 또는 비밀번호가 일치하지 않습니다."
                ));

        // 4) 토큰 생성 후 정상 응답
        String token = jwtUtil.generateToken(
                user.getEmail(),
                List.of("USER")
        );

        Map<String, Object> responseBody = Map.of(
                "token", token,
                "user", Map.of(
                        "userId", user.getId(),
                    "userNickname", user.getNickname(),
                    "userEmail", user.getEmail()
                )
        );

        return ResponseEntity.ok(responseBody);
    }
}
