package com.ulicae.cinelog;

import com.ulicae.cinelog.dto.KinoDto;
import com.uwetrottmann.tmdb2.entities.Movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class KinoBuilderFromMovie {

    public KinoDto build(Movie movie) {
        // TODO take care of locale
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        String yearAsString = "";
        int yearAsInt = 0;
        if (movie.release_date != null) {
            yearAsString = sdf.format(movie.release_date);
            yearAsInt = Integer.parseInt(yearAsString);
        }

        return new KinoDto(
                null,
                movie.id.longValue(),
                movie.title,
                null,
                null,
                null,
                null,
                movie.poster_path,
                movie.overview,
                yearAsInt,
                yearAsString
        );
    }
}
