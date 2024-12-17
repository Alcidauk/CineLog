package com.ulicae.cinelog;

import static org.junit.Assert.assertEquals;

import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.TvShow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

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
public class SerieBuilderFromMovieTest {

    @Test
    public void build() throws Exception {
        TvShow tvShow = new TvShow();

        tvShow.first_air_date = new Date(0);
        tvShow.name = "a new title";
        tvShow.id = 4562131;
        tvShow.poster_path = "a path to the poster";

        SerieDto serieDto = new SerieDto(
                null,
                4562131L,
                null,
                "a new title",
                null,
                null,
                null,
                null,
                "a path to the poster",
                null,
                1970,
                "1970",
                null,
                null
        );

        assertEquals(
                serieDto,
                new SerieBuilderFromMovie().build(tvShow)
        );
    }

    @Test(expected = NullPointerException.class)
    public void build_nullMovie() {
        new SerieBuilderFromMovie().build(null);
    }
}