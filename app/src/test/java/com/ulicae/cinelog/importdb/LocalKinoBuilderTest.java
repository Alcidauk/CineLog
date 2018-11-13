package com.ulicae.cinelog.importdb;

import android.content.Context;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.dao.TmdbKino;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

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
public class LocalKinoBuilderTest {

    @Mock
    private CSVRecord csvRecord;

    @Mock
    private Context context;

    @Test
    public void build() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        TmdbKino tmdbKino = new TmdbKino();
        tmdbKino.setYear(2015);
        tmdbKino.setRelease_date("date release");
        tmdbKino.setOverview("overview");
        tmdbKino.setMovie_id(24L);
        tmdbKino.setPoster_path("poster path");

        assertEquals(
                new LocalKino(
                        3f,
                        "review",
                        "title",
                        simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                        tmdbKino
                ),
                new LocalKinoBuilder(context).build(csvRecord)
        );
    }

    @Test
    public void build_specialFormats() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3,3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        TmdbKino tmdbKino = new TmdbKino();
        tmdbKino.setYear(2015);
        tmdbKino.setRelease_date("date release");
        tmdbKino.setOverview("overview");
        tmdbKino.setMovie_id(24L);
        tmdbKino.setPoster_path("poster path");

        assertEquals(
                new LocalKino(
                        3.3f,
                        "review",
                        "title",
                        simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                        tmdbKino
                ),
                new LocalKinoBuilder(context).build(csvRecord)
        );
    }

    @Test
    public void buildnull_onLong() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn(null).when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3,3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        TmdbKino tmdbKino = new TmdbKino();
        tmdbKino.setYear(2015);
        tmdbKino.setRelease_date("date release");
        tmdbKino.setOverview("overview");
        tmdbKino.setMovie_id(0L);
        tmdbKino.setPoster_path("poster path");

        assertEquals(
                new LocalKino(
                        3.3f,
                        "review",
                        "title",
                        simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                        tmdbKino
                ),
                new LocalKinoBuilder(context).build(csvRecord)
        );
    }

    @Test
    public void buildnull_onFloat() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn(null).when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        TmdbKino tmdbKino = new TmdbKino();
        tmdbKino.setYear(2015);
        tmdbKino.setRelease_date("date release");
        tmdbKino.setOverview("overview");
        tmdbKino.setMovie_id(24L);
        tmdbKino.setPoster_path("poster path");

        assertEquals(
                new LocalKino(
                        0f,
                        "review",
                        "title",
                        simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                        tmdbKino
                ),
                new LocalKinoBuilder(context).build(csvRecord)
        );
    }

}