package org.dfpl.lecture.db.backend.scheduler;

import jakarta.annotation.PostConstruct; // jakarta 버전 주의
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MovieScheduler {

    private final MovieService movieService;

    public MovieScheduler(MovieService movieService) {
        this.movieService = movieService;
    }

    // 매일 오전 3시에 자동 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void savePopularMoviesDaily() {
        int maxPages = 2;
        for (int page = 1; page <= maxPages; page++) {
            movieService.fetchAndSavePopularMovies(page);
            try {
                Thread.sleep(300); // TMDB API 제한 우회
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("✅ TMDB 인기 영화 자동 저장 완료");
    }
}
