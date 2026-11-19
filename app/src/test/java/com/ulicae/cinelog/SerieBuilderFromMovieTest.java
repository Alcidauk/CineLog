package com.ulicae.cinelog;

import static org.junit.Assert.assertEquals;

import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.uwetrottmann.tmdb2.TmdbHelper;
import com.uwetrottmann.tmdb2.entities.TvShow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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

    private static int yearInTimeZone(String timeZoneId, String firstAirDate) throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));

        TvShow tvShow = new TvShow();
        tvShow.id = 4562131;
        tvShow.first_air_date = tmdbDate(firstAirDate);

        return new SerieBuilderFromMovie().build(tvShow).getYear();
    }

    @Test
    public void build() throws Exception {
        TvShow tvShow = new TvShow();

        tvShow.first_air_date = tmdbDate("1970-01-01");
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
                new ArrayList<>(),
                new ArrayList<>()
        );

        assertEquals(
                serieDto,
                new SerieBuilderFromMovie().build(tvShow)
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
    public void build_nullMovie() {
        new SerieBuilderFromMovie().build(null);
    }
}