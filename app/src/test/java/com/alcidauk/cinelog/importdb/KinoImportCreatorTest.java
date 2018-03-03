package com.alcidauk.cinelog.importdb;

import com.alcidauk.cinelog.dao.LocalKino;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class KinoImportCreatorTest {

    @Mock
    private CSVFormatWrapper csvFormatWrapper;

    @Mock
    private FileReader fileReader;

    @Mock
    private CSVRecord record;

    // TODO add some more specific test cases

    @Test
    public void getKinos() throws Exception {
        doReturn(new ArrayList<CSVRecord>(){{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(record).get("movie_id");
        doReturn("title").when(record).get("title");
        doReturn("overview").when(record).get("overview");
        doReturn("2015").when(record).get("year");
        doReturn("poster path").when(record).get("poster_path");
        doReturn("3").when(record).get("rating");
        doReturn("date release").when(record).get("release_date");
        doReturn("review").when(record).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(record).get("review_date");

        assertEquals(
                new ArrayList<LocalKino>(){{
                    add(new LocalKino(
                            "poster path",
                            3,
                            "review",
                            "overview",
                            2015,
                            "title",
                            "date release",
                            24,
                            simpleDateFormat.parse(simpleDateFormat.format(reviewDate))
                    ));
                }},
                new KinoImportCreator(csvFormatWrapper).getKinos(fileReader)
        );
    }

    @Test
    public void getKinos_specialFormats() throws Exception {
        doReturn(new ArrayList<CSVRecord>(){{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(record).get("movie_id");
        doReturn("title").when(record).get("title");
        doReturn("overview").when(record).get("overview");
        doReturn("2015").when(record).get("year");
        doReturn("poster path").when(record).get("poster_path");
        doReturn("3,3").when(record).get("rating");
        doReturn("date release").when(record).get("release_date");
        doReturn("review").when(record).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(record).get("review_date");

        assertEquals(
                new ArrayList<LocalKino>(){{
                    add(new LocalKino(
                            "poster path",
                            3.3f,
                            "review",
                            "overview",
                            2015,
                            "title",
                            "date release",
                            24,
                            simpleDateFormat.parse(simpleDateFormat.format(reviewDate))
                    ));
                }},
                new KinoImportCreator(csvFormatWrapper).getKinos(fileReader)
        );
    }

    @Test
    public void getKinos_null_onInteger() throws Exception {
        doReturn(new ArrayList<CSVRecord>(){{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn(null).when(record).get("movie_id");
        doReturn("title").when(record).get("title");
        doReturn("overview").when(record).get("overview");
        doReturn("2015").when(record).get("year");
        doReturn("poster path").when(record).get("poster_path");
        doReturn("3,3").when(record).get("rating");
        doReturn("date release").when(record).get("release_date");
        doReturn("review").when(record).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(record).get("review_date");

        assertEquals(
                new ArrayList<LocalKino>(){{
                    add(new LocalKino(
                            "poster path",
                            3.3f,
                            "review",
                            "overview",
                            2015,
                            "title",
                            "date release",
                            0,
                            simpleDateFormat.parse(simpleDateFormat.format(reviewDate))
                    ));
                }},
                new KinoImportCreator(csvFormatWrapper).getKinos(fileReader)
        );
    }

    @Test
    public void getKinos_null_onFloat() throws Exception {
        doReturn(new ArrayList<CSVRecord>(){{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        final Date reviewDate = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        doReturn("24").when(record).get("movie_id");
        doReturn("title").when(record).get("title");
        doReturn("overview").when(record).get("overview");
        doReturn("2015").when(record).get("year");
        doReturn("poster path").when(record).get("poster_path");
        doReturn(null).when(record).get("rating");
        doReturn("date release").when(record).get("release_date");
        doReturn("review").when(record).get("review");
        doReturn(simpleDateFormat.format(reviewDate)).when(record).get("review_date");

        assertEquals(
                new ArrayList<LocalKino>(){{
                    add(new LocalKino(
                            "poster path",
                            0f,
                            "review",
                            "overview",
                            2015,
                            "title",
                            "date release",
                            24,
                            simpleDateFormat.parse(simpleDateFormat.format(reviewDate))
                    ));
                }},
                new KinoImportCreator(csvFormatWrapper).getKinos(fileReader)
        );
    }

    /*Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(in);
        for (CSVRecord record : records) {
            String author = record.get("author");
            String title = record.get("title");
            assertEquals(AUTHOR_BOOK_MAP.get(author), title);
        }*/

}