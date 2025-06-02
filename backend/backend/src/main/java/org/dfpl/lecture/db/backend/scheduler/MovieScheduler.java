package org.dfpl.lecture.db.backend.scheduler;

import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MovieScheduler {

    private final MovieService movieService;

    public MovieScheduler(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * 매일 새벽 3시에 크롤링을 실행합니다.
     * - totalPages는 원하는 만큼 조정하세요 (예: 5페이지 → 약 100개 영화)
     * - @Scheduled 표현식을 바꿔서 다른 시간대/주기로 설정할 수 있습니다.
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void dailyCrawl() {
        int totalPages = 5;
        System.out.println(">>> [스케줄러] TMDb 인기 영화 크롤링 시작 (총 " + totalPages + " 페이지)");
        movieService.crawlPopularMovies(totalPages);
        System.out.println(">>> [스케줄러] TMDb 크롤링 완료");
    }
}
