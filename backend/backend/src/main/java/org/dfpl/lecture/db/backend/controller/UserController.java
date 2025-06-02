package org.dfpl.lecture.db.backend.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.dfpl.lecture.db.backend.dto.FavoriteMovieDTO;
import org.dfpl.lecture.db.backend.dto.MyPageDTO;
import org.dfpl.lecture.db.backend.dto.MyReviewDTO;
import org.dfpl.lecture.db.backend.dto.ReviewResponse;
import org.dfpl.lecture.db.backend.entity.Favorite;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.dfpl.lecture.db.backend.service.FavoriteService;
import org.dfpl.lecture.db.backend.service.ReviewService;
import org.dfpl.lecture.db.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FavoriteService favoriteService;

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

    @GetMapping("/me/favorites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FavoriteMovieDTO>> getMyFavorites(Authentication auth) {
        String email = auth.getName();
        List<FavoriteMovieDTO> favorites = favoriteService.findFavoritesByEmail(email);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MyPageDTO> myPage(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + email));

        MyPageDTO dto = userService.getMyPage(user);

        List<Favorite> allFavs = favoriteService.getFavoritesByUser(user);
        long favoriteCount = allFavs.size();

        List<FavoriteMovieDTO> top3Favs = allFavs.stream()
                .sorted(Comparator.comparing(Favorite::getCreatedAt).reversed())
                .limit(3)
                .map(fav -> new FavoriteMovieDTO(
                        fav.getMovie().getId(),
                        fav.getMovie().getTitle(),
                        fav.getMovie().getPosterPath()
                ))
                .collect(Collectors.toList());

        dto.setFavoriteCount(favoriteCount);
        dto.setFavoriteMovies(top3Favs);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/me/favorites/{movieId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addFavorite(
            Authentication auth,
            @PathVariable("movieId") Long movieId
    ) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + email));

        try {
            favoriteService.addFavorite(user, movieId);
            return ResponseEntity.ok().body("{ \"message\": \"즐겨찾기에 추가되었습니다.\" }");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("{ \"error\": \"" + e.getMessage() + "\" }");
        }
    }

    @DeleteMapping("/me/favorites/{movieId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> removeFavorite(
            Authentication auth,
            @PathVariable("movieId") Long movieId
    ) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + email));

        try {
            favoriteService.removeFavorite(user, movieId);
            return ResponseEntity.ok().body("{ \"message\": \"즐겨찾기가 해제되었습니다.\" }");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("{ \"error\": \"" + e.getMessage() + "\" }");
        }
    }
}

