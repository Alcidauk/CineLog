package com.ulicae.cinelog.io.exportdb.writer;

import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class SerieCsvExportWriterTest {

    @Mock
    private CSVPrinterWrapper csvPrinterWrapper;

    @Mock
    private SerieDto aKino;

    @Test
    public void write() throws Exception {
        List<TagDto> tags = new ArrayList<>();
        tags.add(new TagDto(4L, "horreur", "#435436", false, true));
        tags.add(new TagDto(7L, "malheur", "#435436", false, true));

        Date aDate = new Date();
        aKino = new SerieDto(
               24L,
               25L,
               321231L,
               "a title",
                aDate,
                "a review",
                2.4f,
                5,
                "/path",
                "an overview",
                1984,
                "2012",
                tags
        );

        new SerieCsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                24L,
                25L,
                321231L,
                "a title",
                "an overview",
                1984,
                "/path",
                2.4f,
                "2012",
                "a review",
                new SimpleDateFormat().format(aDate),
                5,
                "4,7"
        );
    }

    @Test
    public void writeNullReviewDate() throws Exception {
        aKino = new SerieDto(
                24L,
                25L,
                321231L,
                "a title",
                null,
                "a review",
                2.4f,
                5,
                "/path",
                "an overview",
                1984,
                "2012",
                null
        );

        new SerieCsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(
                24L,
                25L,
                321231L,
                "a title",
                "an overview",
                1984,
                "/path",
                2.4f,
                "2012",
                "a review",
                null,
                5,
                ""
        );
    }

    @Test
    public void endWriting() throws Exception {
        new SerieCsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}