package org.dfpl.lecture.db.backend.scheduler;

import lombok.RequiredArgsConstructor;
import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieScheduler {

    private final MovieService movieService;

    /**
     * 매일 오전 3시에 TMDb 인기 영화 5페이지까지 순차적으로 가져와 DB에 저장
     * (Asia/Seoul 기준)
     */
    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Seoul")
    public void fetchDailyPopularMovies() {
        int totalPages = 5;
        // 기존 crawlPopularMovies(totalPages) → fetchAndSavePopularMovies(1, totalPages)로 변경
        movieService.fetchAndSavePopularMovies(1, totalPages);
    }
}
