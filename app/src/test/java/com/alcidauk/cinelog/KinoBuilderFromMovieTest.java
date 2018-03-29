package com.alcidauk.cinelog;

import com.alcidauk.cinelog.dto.KinoDto;
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

        KinoDto kinoDto = new KinoDto(
                null,
                4562131L,
                "a new title",
                null,
                null,
                null,
                null,
                "a path to the poster",
                "an overview",
                1970,
                "1970"
        );

        assertEquals(
                kinoDto,
                new KinoBuilderFromMovie().build(movie)
        );
    }

    @Test(expected = NullPointerException.class)
    public void build_nullMovie() throws Exception {
        new KinoBuilderFromMovie().build(null);
    }
}