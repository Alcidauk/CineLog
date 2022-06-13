package com.ulicae.cinelog.io.importdb.builder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import android.content.Context;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.io.importdb.ImportException;
import com.ulicae.cinelog.utils.PreferencesWrapper;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagDtoFromRecordBuilderTest {

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
        doReturn("horreur").when(csvRecord).get("name");
        doReturn("`#456789`").when(csvRecord).get("color");
        doReturn("true").when(csvRecord).get("forMovies");
        doReturn("false").when(csvRecord).get("forSeries");

        TagDto dataDto = new TagDto(
                12L,
                "horreur",
                "#456789",
                true,
                false
        );

        assertEquals(
                dataDto,
                new TagDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }

    @Test
    public void buildForMoviesNotABoolean() throws Exception {
        doReturn(true).when(csvRecord).isMapped("id");
        doReturn("12").when(csvRecord).get("id");
        doReturn("horreur").when(csvRecord).get("name");
        doReturn("`#456789`").when(csvRecord).get("color");
        doReturn("troupe").when(csvRecord).get("forMovies");
        doReturn("false").when(csvRecord).get("forSeries");

        TagDto dataDto = new TagDto(
                12L,
                "horreur",
                "#456789",
                false,
                false
        );

        assertEquals(
                dataDto,
                new TagDtoFromRecordBuilder(context, preferencesWrapper).build(csvRecord)
        );
    }


}