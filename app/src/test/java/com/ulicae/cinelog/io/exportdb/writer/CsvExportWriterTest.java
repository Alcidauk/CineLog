package com.ulicae.cinelog.io.exportdb.writer;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.io.exportdb.writer.CSVPrinterWrapper;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;

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
    private KinoDto aKino;

    @Test
    public void write() throws Exception {
        Date aDate = new Date();
        aKino = new KinoDto(
               24L,
               25L,
               "a title",
                aDate,
                "a review",
                2.4f,
                5,
                "/path",
                "an overview",
                1984,
                "2012"
        );

        new CsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                24L,
                25L,
                "a title",
                "an overview",
                1984,
                "/path",
                2.4f,
                "2012",
                "a review",
                new SimpleDateFormat().format(aDate),
                5
        );
    }

    @Test
    public void writeNullReviewDate() throws Exception {
        aKino = new KinoDto(
                24L,
                25L,
                "a title",
                null,
                "a review",
                2.4f,
                5,
                "/path",
                "an overview",
                1984,
                "2012"
        );

        new CsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                24L,
                25L,
                "a title",
                "an overview",
                1984,
                "/path",
                2.4f,
                "2012",
                "a review",
                null,
                5
        );
    }

    @Test
    public void endWriting() throws Exception {
        new CsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}