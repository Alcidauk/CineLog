package com.ulicae.cinelog.importdb;

import android.content.Context;

import com.ulicae.cinelog.dao.LocalKino;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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
public class KinoImportCreatorTest {

    @Mock
    private CSVFormatWrapper csvFormatWrapper;

    @Mock
    private FileReader fileReader;

    @Mock
    private CSVRecord record;

    @Mock
    private LocalKino localKino;

    @Mock
    private LocalKinoBuilder localKinoBuilder;

    @Mock
    private Context context;

    @Test
    public void getKinos() throws Exception {
        doReturn(new ArrayList<CSVRecord>() {{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        doReturn(localKino).when(localKinoBuilder).build(record);

        assertEquals(
                Collections.singletonList(localKino),
                new KinoImportCreator(csvFormatWrapper, localKinoBuilder, context).getKinos(fileReader)
        );
    }

    @Test(expected = ImportException.class)
    public void getKinos_ioException_onWrapper() throws Exception {
        doThrow(new IOException()).when(csvFormatWrapper).parse(fileReader);

        assertEquals(
                Collections.singletonList(localKino),
                new KinoImportCreator(csvFormatWrapper, localKinoBuilder, context).getKinos(fileReader)
        );
    }

}