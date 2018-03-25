package com.alcidauk.cinelog;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KinoBuilderFromMovieTest {

    @Test
    public void build() throws Exception {
        Movie movie = new Movie();

        movie.release_date = new Date(0);
        movie.title = "a new title";
        movie.id = 4562131;
        movie.poster_path = "a path to the poster";
        movie.overview = "an overview";

        TmdbKino tmdbKino = new TmdbKino(
                4562131L,
                "a path to the poster",
                "an overview",
                1970,
                "1970"
        );
        LocalKino kino = new LocalKino("a new title", tmdbKino);

        assertEquals(
                kino,
                new KinoBuilderFromMovie().build(movie)
        );

    }

    @Test(expected = NullPointerException.class)
    public void build_nullMovie() throws Exception {
        new KinoBuilderFromMovie().build(null);
    }
}