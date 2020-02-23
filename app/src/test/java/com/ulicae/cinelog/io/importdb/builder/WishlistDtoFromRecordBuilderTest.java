package com.ulicae.cinelog.io.importdb.builder;

import android.content.Context;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class WishlistDtoFromRecordBuilderTest {

    @Mock
    private CSVRecord csvRecord;

    @Mock
    private Context context;

    @Mock
    private PreferencesWrapper preferencesWrapper;

    @Test
    public void build() throws Exception {
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("12").when(csvRecord).get("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("a good title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("firstYear");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("MOVIE").when(csvRecord).get("wishlistItemType");


        WishlistDataDto dataDto = new WishlistDataDto(
                12L,
                24,
                "a good title",
                "poster path",
                "overview",
                2015,
                "date release",
                WishlistItemType.MOVIE
        );

        assertEquals(
                dataDto,
                new WishlistDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void build_serie() throws Exception {
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("12").when(csvRecord).get("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("a good title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("firstYear");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("SERIE").when(csvRecord).get("wishlistItemType");


        WishlistDataDto dataDto = new WishlistDataDto(
                12L,
                24,
                "a good title",
                "poster path",
                "overview",
                2015,
                "date release",
                WishlistItemType.SERIE
        );

        assertEquals(
                dataDto,
                new WishlistDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test(expected = ImportException.class)
    public void build_badType() throws Exception {
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("12").when(csvRecord).get("id");
        doReturn("24").when(csvRecord).get("movie_id");
        doReturn("a good title").when(csvRecord).get("title");
        doReturn("overview").when(csvRecord).get("overview");
        doReturn("2015").when(csvRecord).get("firstYear");
        doReturn("poster path").when(csvRecord).get("poster_path");
        doReturn("date release").when(csvRecord).get("release_date");
        doReturn("banane").when(csvRecord).get("wishlistItemType");

        new WishlistDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord);
    }

}