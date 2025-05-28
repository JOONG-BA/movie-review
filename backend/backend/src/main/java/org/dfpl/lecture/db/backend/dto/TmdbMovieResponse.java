package org.dfpl.lecture.db.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TmdbMovieResponse {
    private int page;
    private List<MovieResult> results;

    @Data
    public static class MovieResult {
        private Long id;
        private String title;
        private String overview;
        private String release_date;
        private String poster_path;
        private Double vote_average;
    }
}
