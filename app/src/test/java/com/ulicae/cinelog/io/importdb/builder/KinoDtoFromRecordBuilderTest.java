package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.PreferencesWrapper;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
@RunWith(MockitoJUnitRunner.class)
public class KinoDtoFromRecordBuilderTest {

    @Mock
    private CSVRecord csvRecord;

    @Mock
    private Context context;

    @Mock
    private PreferencesWrapper preferencesWrapper;

    @Test
    public void build() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(true).when(csvRecord).isMapped("max_rating");
        doReturn("5").when(csvRecord).get("max_rating");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");
        doReturn("1,3").when(csvRecord).get("tags");

        List<TagDto> tags = new ArrayList<TagDto>() {{
            add(new TagDto(1L, null, null, false, false));
            add(new TagDto(3L, null, null, false, false));
        }};

        KinoDto kinoDto = new KinoDto(
                12L,
                24L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                3f,
                5,
                "poster path",
                "overview",
                2015,
                "date release",
                tags
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void build_specialFormats() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3,3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(true).when(csvRecord).isMapped("max_rating");
        doReturn("10").when(csvRecord).get("max_rating");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        KinoDto kinoDto = new KinoDto(
                12L,
                24L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                3.3f,
                10,
                "poster path",
                "overview",
                2015,
                "date release",
                null
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void buildnull_onLong() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn(null).when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3,3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(true).when(csvRecord).isMapped("max_rating");
        doReturn("5").when(csvRecord).get("max_rating");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        KinoDto kinoDto = new KinoDto(
                12L,
                0L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                3.3f,
                5,
                "poster path",
                "overview",
                2015,
                "date release",
                null
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void buildnull_onFloat() throws Exception {
        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn(null).when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(true).when(csvRecord).isMapped("max_rating");
        doReturn("5").when(csvRecord).get("max_rating");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");


        KinoDto kinoDto = new KinoDto(
                12L,
                24L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                0f,
                5,
                "poster path",
                "overview",
                2015,
                "date release",
                null
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void build_missingMaxRating() throws Exception {
        doReturn("10").when(preferencesWrapper).getStringPreference(context, "default_max_rate_value", "5");

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        KinoDto kinoDto = new KinoDto(
                12L,
                24L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                3f,
                10,
                "poster path",
                "overview",
                2015,
                "date release",
                null
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void build_missingTags() throws Exception {
        doThrow(new IllegalArgumentException()).when(csvRecord).get("tags");

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("12").when(csvRecord).get("id");
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("year");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("3").when(csvRecord).get("rating");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("review").when(csvRecord).get("review");
        doReturn(true).when(csvRecord).isMapped("max_rating");
        doReturn("5").when(csvRecord).get("max_rating");
        doReturn(simpleDateFormat.format(reviewDate)).when(csvRecord).get("review_date");

        KinoDto kinoDto = new KinoDto(
                12L,
                24L,
                "title",
                simpleDateFormat.parse(simpleDateFormat.format(reviewDate)),
                "review",
                3f,
                5,
                "poster path",
                "overview",
                2015,
                "date release",
                null
        );

        assertEquals(
                kinoDto,
                new KinoDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

}