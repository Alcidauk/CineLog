package com.ulicae.cinelog;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
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
                "1970",
                null
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