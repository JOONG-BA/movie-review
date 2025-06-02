package org.dfpl.lecture.db.backend;

import org.dfpl.lecture.db.backend.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initCrawler(MovieService movieService) {
//        return args -> {
//            int totalPages = 5;  // 예시: 인기 영화 5페이지(약 100개 영화) 크롤링
//            System.out.println(">>> TMDb 인기 영화 크롤링 시작 (총 " + totalPages + " 페이지)");
//            movieService.crawlPopularMovies(totalPages);
//            System.out.println(">>> TMDb 크롤링 완료");
//        };
//    }
}
