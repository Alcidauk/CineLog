package com.ulicae.cinelog.importdb;

import android.app.Activity;
import android.content.Context;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.db.LocalKinoRepository;

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
public class CsvImporterTest {

    @Mock
    private FileReaderGetter fileReaderGetter;
    @Mock
    private FileReader fileReader;

    @Mock
    private KinoImportCreator kinoImportCreator;

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Mock
    private LocalKino aLocalKino;
    @Mock
    private LocalKino anotherLocalKino;

    @Mock
    private Context context;

    @Test
    public void importCsvFile() throws Exception {
        doReturn(fileReader).when(fileReaderGetter).get("import.csv");

        doReturn(new ArrayList<LocalKino>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }}).when(kinoImportCreator).getKinos(fileReader);

        new CsvImporter(fileReaderGetter, kinoImportCreator, localKinoRepository, context).importCsvFile();

        verify(localKinoRepository).createOrUpdate(new ArrayList<LocalKino>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }});
    }
}