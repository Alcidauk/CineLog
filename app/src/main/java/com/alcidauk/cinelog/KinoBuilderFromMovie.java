package com.alcidauk.cinelog;

import android.content.Intent;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.uwetrottmann.tmdb2.entities.Movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class KinoBuilderFromMovie {

    public LocalKino build(Movie movie) {
        // TODO take care of locale
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        String yearAsString = "";
        int yearAsInt = 0;
        if (movie.release_date != null) {
            yearAsString = sdf.format(movie.release_date);
            yearAsInt = Integer.parseInt(yearAsString);
        }

        TmdbKino tmdbKino = new TmdbKino(
                movie.id.longValue(),
                movie.poster_path,
                movie.overview,
                yearAsInt,
                yearAsString
        );

        return new LocalKino(movie.title, tmdbKino);
    }
}
