package com.alcidauk.cinelog.importdb;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class LocalKinoBuilderTest {

    @Mock
    private CSVRecord csvRecord;

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
                new LocalKinoBuilder().build(csvRecord)
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
                new LocalKinoBuilder().build(csvRecord)
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
                new LocalKinoBuilder().build(csvRecord)
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
                new LocalKinoBuilder().build(csvRecord)
        );
    }

}