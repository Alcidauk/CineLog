package com.ulicae.cinelog.io.exportdb.writer;

import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.data.dto.TagDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
public class TagCsvExportWriterTest {

    @Mock
    private CSVPrinterWrapper csvPrinterWrapper;

    @Mock
    private TagDto aTag;

    @Test
    public void write() throws Exception {
        aTag = new TagDto(24L, "Horreur", "#456789", false, true);

        new TagCsvExportWriter(csvPrinterWrapper).write(aTag);

        verify(csvPrinterWrapper).printRecord(
                24L,
                "Horreur",
                "#456789",
                false,
                true
        );
    }

    @Test
    public void endWriting() throws Exception {
        new TagCsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}