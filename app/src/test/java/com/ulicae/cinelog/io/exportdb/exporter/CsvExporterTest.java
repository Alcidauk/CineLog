package com.ulicae.cinelog.io.exportdb.exporter;

import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvExporterTest {

    @Mock
    private KinoService kinoService;

    @Mock
    private CsvExportWriter csvExportWriter;

    @Test
    public void export() throws Exception {
        final KinoDto aKino = mock(KinoDto.class);
        final KinoDto anotherKino = mock(KinoDto.class);
        ArrayList<KinoDto> kinos = new ArrayList<KinoDto>() {{
            add(aKino);
            add(anotherKino);
        }};
        doReturn(kinos).when(kinoService).getAll();

        new CsvExporter(kinoService, csvExportWriter).export();

        verify(csvExportWriter).write(aKino);
        verify(csvExportWriter).write(anotherKino);

        verify(csvExportWriter).endWriting();
    }
}