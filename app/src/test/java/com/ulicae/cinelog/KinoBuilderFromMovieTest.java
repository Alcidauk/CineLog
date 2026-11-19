package com.ulicae.cinelog;

import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.uwetrottmann.tmdb2.TmdbHelper;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

    private TimeZone defaultTimeZone;

    @Before
    public void saveDefaultTimeZone() {
        defaultTimeZone = TimeZone.getDefault();
    }

    @After
    public void restoreDefaultTimeZone() {
        TimeZone.setDefault(defaultTimeZone);
    }

    /**
     * Parse a date the same way tmdb-java does: "yyyy-MM-dd" in the
     * default time zone, so the result is local midnight.
     */
    private static Date tmdbDate(String date) throws ParseException {
        return new SimpleDateFormat(TmdbHelper.TMDB_DATE_PATTERN).parse(date);
    }

    private static int yearInTimeZone(String timeZoneId, String releaseDate) throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));

        Movie movie = new Movie();
        movie.id = 4562131;
        movie.release_date = tmdbDate(releaseDate);

        return new KinoBuilderFromMovie().build(movie).getYear();
    }

    @Test
    public void build() throws Exception {
        Movie movie = new Movie();

        movie.release_date = tmdbDate("1970-01-01");
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

    /* West of UTC: local midnight on 1 January is still 1 January in UTC. */
    @Test
    public void build_yearAtNewYearInVancouver() throws Exception {
        assertEquals(2020, yearInTimeZone("America/Vancouver", "2020-01-01"));
    }

    /* East of UTC: local midnight on 1 January is 31 December in UTC. */
    @Test
    public void build_yearAtNewYearInAuckland() throws Exception {
        assertEquals(2020, yearInTimeZone("Pacific/Auckland", "2020-01-01"));
    }

    @Test(expected = NullPointerException.class)
    public void build_nullMovie() throws Exception {
        new KinoBuilderFromMovie().build(null);
    }
}