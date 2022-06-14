package com.ulicae.cinelog.io.importdb;

import android.content.Context;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.KinoService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
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
public class CsvImporterTest {

    @Mock
    private FileReaderGetter fileReaderGetter;
    @Mock
    private FileReader fileReader;

    @Mock
    private DtoImportCreator dtoImportCreator;

    @Mock
    private KinoService kinoService;

    @Mock
    private KinoDto aLocalKino;
    @Mock
    private KinoDto anotherLocalKino;

    @Mock
    private Context context;

    @Test
    public void importCsvFile() throws Exception {
        doReturn(fileReader).when(fileReaderGetter).get("import.csv");

        doReturn(new ArrayList<KinoDto>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }}).when(dtoImportCreator).getDtos(fileReader);

        new CsvImporter(fileReaderGetter, dtoImportCreator, kinoService, context).importCsvFile("import.csv");

        verify(kinoService).createOrUpdateFromImport(new ArrayList<KinoDto>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }});
    }
}