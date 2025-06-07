package org.dfpl.lecture.db.backend.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.dto.FavoriteMovieDTO;
import org.dfpl.lecture.db.backend.entity.Favorite;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.repository.FavoriteRepository;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<FavoriteMovieDTO> findFavoritesByEmail(String email) {
        // (1) 이메일로 User 엔티티 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: email=" + email));

        // (2) User 엔티티로 즐겨찾기 목록(Favorite 엔티티)을 모두 가져오기
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);

        // (3) Favorite → FavoriteMovieDTO로 변환
        return favorites.stream()
                .map(fav -> {
                    MovieDB m = fav.getMovie();
                    return new FavoriteMovieDTO(
                            m.getId(),            // movieId
                            m.getTitle(),         // title
                            m.getPosterUrl()     // posterPath
                            // 필요하다면 추가 필드(getReleaseDate(), getVoteAverage() 등)도 넣으세요
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Favorite addFavorite(User user, Long movieId) {
        MovieDB movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: id=" + movieId));

        favoriteRepository.findByUserAndMovie(user, movie)
                .ifPresent(fav -> {
                    throw new IllegalArgumentException("이미 즐겨찾기된 영화입니다: movieId=" + movieId);
                });

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMovie(movie);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(User user, Long movieId) {
        MovieDB movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: id=" + movieId));

        Favorite favorite = favoriteRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기된 영화가 아닙니다: id=" + movieId));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(String email, Long movieId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));
        MovieDB movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: id=" + movieId));
        return favoriteRepository.existsByUserAndMovie(user, movie);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(User user, Long movieId) {
        MovieDB movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: id=" + movieId));
        return favoriteRepository.existsByUserAndMovie(user, movie);
    }
}
