package com.ulicae.cinelog.io.importdb;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.documentfile.provider.DocumentFile;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.services.ReviewAsyncService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public class AsyncCsvImporterTest {

    @Mock
    private FileReaderGetter fileReaderGetter;
    @Mock
    private FileReader fileReader;
    @Mock
    private DocumentFile documentFile;

    @Mock
    private DtoImportCreator dtoImportCreator;

    @Mock
    private ReviewAsyncService reviewAsyncService;

    @Mock
    private KinoDto aLocalKino;
    @Mock
    private KinoDto anotherLocalKino;

    @Mock
    private Context context;

    @Mock
    private CinelogSchedulers cinelogSchedulers;


    @Before
    public void before() {
        doReturn(Schedulers.trampoline()).when(cinelogSchedulers).androidMainThread();
        doReturn(Schedulers.trampoline()).when(cinelogSchedulers).io();
    }


    @Test
    public void importCsvFile() throws Exception {
        DocumentFile docFile = mock(DocumentFile.class);
        doReturn(docFile).when(documentFile).findFile("import.csv");

        doReturn(fileReader).when(fileReaderGetter).get(docFile);

        ArrayList<KinoDto> dtos = new ArrayList<KinoDto>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }};
        doReturn(dtos).when(dtoImportCreator).getDtos(fileReader);

        doReturn(Completable.complete()).when(reviewAsyncService).createOrUpdate(dtos);

        new AsyncCsvImporter<>(fileReaderGetter, dtoImportCreator, reviewAsyncService, context)
                .importCsvFile(documentFile, "import.csv");

        verify(reviewAsyncService).createOrUpdate(dtos);
    }
}