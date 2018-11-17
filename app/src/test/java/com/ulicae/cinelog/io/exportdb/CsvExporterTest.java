package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.data.dao.LocalKino;
import com.ulicae.cinelog.data.db.LocalKinoRepository;

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
public class CsvExporterTest {

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Mock
    private CsvExportWriter csvExportWriter;

    @Test
    public void export() throws Exception {
        final LocalKino aKino = mock(LocalKino.class);
        final LocalKino anotherKino = mock(LocalKino.class);
        ArrayList<LocalKino> kinos = new ArrayList<LocalKino>() {{
            add(aKino);
            add(anotherKino);
        }};
        doReturn(kinos).when(localKinoRepository).findAll();

        new CsvExporter(localKinoRepository, csvExportWriter).export();

        verify(csvExportWriter).write(aKino);
        verify(csvExportWriter).write(anotherKino);

        verify(csvExportWriter).endWriting();
    }
}