package org.dfpl.lecture.db.backend.service;

import lombok.RequiredArgsConstructor;
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
}
