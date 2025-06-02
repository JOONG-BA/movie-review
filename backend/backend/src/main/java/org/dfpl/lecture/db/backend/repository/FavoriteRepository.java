package org.dfpl.lecture.db.backend.repository;

import org.dfpl.lecture.db.backend.entity.Favorite;
import org.dfpl.lecture.db.backend.entity.User;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUser(User user);

    Optional<Favorite> findByUserAndMovie(User user, MovieDB movie);
}
