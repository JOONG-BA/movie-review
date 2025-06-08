package org.dfpl.lecture.db.backend.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.entity.Genre;
import org.dfpl.lecture.db.backend.entity.MovieDB;
import org.dfpl.lecture.db.backend.entity.MovieGenre;
import org.dfpl.lecture.db.backend.entity.MovieGenreId;
import org.dfpl.lecture.db.backend.repository.GenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieGenreRepository;
import org.dfpl.lecture.db.backend.repository.MovieRepository;
import org.dfpl.lecture.db.backend.util.GenreMappingUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreAndMovieGenreLoader implements CommandLineRunner {

    private final GenreRepository genreRepo;
    private final MovieRepository movieRepo;
    private final MovieGenreRepository movieGenreRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 이미 한 번 매핑이 되어 있으면 전체 로직 건너뛰기
        if (movieGenreRepo.count() > 0) {
            return;
        }

        // 1) Genre 테이블 채우기 (없을 때만)
        GenreMappingUtil.GENRE_ID_TO_NAME.forEach((id, name) -> {
            if (!genreRepo.existsById(id)) {
                genreRepo.save(Genre.builder()
                        .id(id)
                        .name(name)
                        .build());
            }
        });

        // 2) MovieGenre 조인 테이블 채우기
        List<MovieDB> movies = movieRepo.findAll();

        // 이미 매핑된 PK 집합 (방지용)
        Set<MovieGenreId> existing = movieGenreRepo.findAll().stream()
                .map(MovieGenre::getId)
                .collect(Collectors.toSet());

        List<MovieGenre> toSave = new ArrayList<>();
        for (MovieDB movie : movies) {
            List<Long> genreIds = Arrays.asList(
                    movie.getGenre1(),
                    movie.getGenre2(),
                    movie.getGenre3(),
                    movie.getGenre4()
            );
            for (Long gid : genreIds) {
                if (gid == null) continue;
                MovieGenreId pk = new MovieGenreId(movie.getId(), gid);
                if (existing.contains(pk)) continue;

                Genre genre = genreRepo.findById(gid)
                        .orElseThrow(() -> new IllegalStateException("Unknown Genre ID: " + gid));

                toSave.add(MovieGenre.builder()
                        .id(pk)
                        .movie(movie)
                        .genre(genre)
                        .build());
            }
        }

        if (!toSave.isEmpty()) {
            movieGenreRepo.saveAll(toSave);
        }
    }
}
