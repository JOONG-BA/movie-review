package org.dfpl.lecture.db.backend.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dfpl.lecture.db.backend.dto.FavoriteMovieDTO;
import org.dfpl.lecture.db.backend.dto.MyPageDTO;
import org.dfpl.lecture.db.backend.dto.MyReviewDTO;
import org.dfpl.lecture.db.backend.entity.Favorite;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.FavoriteRepository;
import org.dfpl.lecture.db.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public MyPageDTO getMyPage(User user) {
        List<Review> all = reviewRepository.findAllByUser(user);
        long reviewCount = all.size();
        List<MyReviewDTO> recent = all.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .map(r -> new MyReviewDTO(
                        r.getId(),
                        r.getMovie().getId(),
                        r.getScore() != null ? r.getScore() : 0.0,
                        r.getContent()
                ))
                .collect(Collectors.toList());

        List<Favorite> allFavs = favoriteRepository.findAllByUser(user);
        long favoriteCount = allFavs.size();

        List<FavoriteMovieDTO> top3Favs = allFavs.stream()
                .sorted(Comparator.comparing(Favorite::getCreatedAt).reversed())
                .map(fav -> new FavoriteMovieDTO(
                        fav.getMovie().getId(),
                        fav.getMovie().getTitle(),
                        fav.getMovie().getPosterUrl()
                ))
                .collect(Collectors.toList());

        // (3) DTO에 담아서 반환
        MyPageDTO dto = new MyPageDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setReviewCount(reviewCount);
        dto.setFavoriteCount(favoriteCount);
        dto.setRecentReviews(recent);
        dto.setFavoriteMovies(top3Favs);
        return dto;
    }

    public List<MyReviewDTO> getMyReviews(User user) {
        return reviewRepository.findAllByUser(user).stream()
                .map(r -> new MyReviewDTO(
                        r.getId(),
                        r.getMovie().getId(),
                        r.getScore(),
                        r.getContent()
                ))
                .collect(Collectors.toList());
    }
}
