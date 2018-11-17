package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.dao.TmdbKino;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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

        Date aDate = new Date();
        //noinspection ResultOfMethodCallIgnored
        doReturn(aDate).when(aKino).getReview_date();

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
                new SimpleDateFormat().format(aKino.getReview_date()),
                aKino.getMaxRating()
        );
    }

    @Test
    public void writeNullReviewDate() throws Exception {
        doReturn(tmdbKino).when(aKino).getKino();
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(aKino).getReview_date();

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
                null,
                aKino.getMaxRating()
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
                aKino.getReview_date(),
                aKino.getMaxRating()
        );
    }

    @Test
    public void endWriting() throws Exception {
        new CsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}