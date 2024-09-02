package com.ulicae.cinelog.io.exportdb.writer;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WishlistCsvExportWriterTest {

    @Mock
    private CSVPrinterWrapper csvPrinterWrapper;

    @Mock
    private WishlistDataDto wishlistDto;

    @Test
    public void write() throws Exception {
        wishlistDto = new WishlistDataDto(
                24L,
                25L,
                "a title",
                "/path",
                "an overview",
                2012,
                "20120212",
                WishlistItemType.MOVIE
        );

        new WishlistCsvExportWriter(csvPrinterWrapper).write(wishlistDto);

        verify(csvPrinterWrapper).printRecord(
                24L,
                25L,
                "a title",
                "an overview",
                2012,
                "/path",
                "20120212",
                "MOVIE"
        );
    }

    @Test
    public void endWriting() throws Exception {
        new WishlistCsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }

}