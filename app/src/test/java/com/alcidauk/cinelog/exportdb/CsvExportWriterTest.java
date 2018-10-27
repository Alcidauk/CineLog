package com.alcidauk.cinelog.exportdb;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.dao.TmdbKino;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CsvExportWriterTest {

    @Mock
    private CSVPrinterWrapper csvPrinterWrapper;

    @Mock
    private LocalKino aKino;

    @Mock
    private TmdbKino tmdbKino;

    @Test
    public void write() throws Exception {
        doReturn(tmdbKino).when(aKino).getKino();

        new CsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                tmdbKino.getMovie_id(),
                aKino.getTitle(),
                tmdbKino.getOverview(),
                tmdbKino.getYear(),
                tmdbKino.getPoster_path(),
                aKino.getRating(),
                tmdbKino.getRelease_date(),
                aKino.getReview(),
                aKino.getReview_date()
        );
    }

    @Test
    public void writeNullTmdbKino() throws Exception {
        doReturn(null).when(aKino).getKino();

        new CsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                null,
                aKino.getTitle(),
                null,
                null,
                null,
                aKino.getRating(),
                null,
                aKino.getReview(),
                aKino.getReview_date()
        );
    }

    @Test
    public void endWriting() throws Exception {
        new CsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}