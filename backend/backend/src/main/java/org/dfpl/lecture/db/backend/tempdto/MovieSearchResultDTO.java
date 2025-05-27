
package org.dfpl.lecture.db.backend.tempdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieSearchResultDTO {
    private Long id;
    private String title;
    private String releaseDate;
    private String posterPath;
    private String productionCountries;
    private String category;
}
