package org.dfpl.lecture.db.backend.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dfpl.lecture.db.backend.dto.MyPageDTO;
import org.dfpl.lecture.db.backend.dto.MyReviewDTO;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ReviewRepository reviewRepository;

    public MyPageDTO getMyPage(User user) {
        List<Review> all = reviewRepository.findAllByUser(user);

        long reviewCount = all.size();

        List<MyReviewDTO> recent = all.stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .limit(3)
                .map(r -> new MyReviewDTO(
                        r.getId(),
                        r.getMovie().getId(),
                        r.getScore(),
                        r.getContent()
                ))
                .collect(Collectors.toList());

        MyPageDTO dto = new MyPageDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setReviewCount(reviewCount);
        dto.setFavoriteCount(0);
        dto.setRecentReviews(recent);
        return dto;
    }

    /**
     * 내가 쓴 리뷰 전체 조회
     */
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
