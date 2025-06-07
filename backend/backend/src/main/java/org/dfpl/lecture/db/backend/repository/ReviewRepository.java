package org.dfpl.lecture.db.backend.repository;

import java.util.List;
import java.util.Optional;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.Review;
import org.dfpl.lecture.db.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser(User user);
    List<Review> findAllByMovie_Id(Long movieId);
    Optional<Review> findByUserAndMovie(User user, MovieDB movie);
}
