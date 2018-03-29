package com.alcidauk.cinelog.importdb;

import android.app.Activity;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by alcidauk on 16/02/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvImporterTest {

    @Mock
    private FileReaderGetter fileReaderGetter;
    @Mock
    private FileReader fileReader;

    @Mock
    private Activity activity;

    @Mock
    private KinoImportCreator kinoImportCreator;

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Mock
    private LocalKino aLocalKino;
    @Mock
    private LocalKino anotherLocalKino;

    @Test
    public void importCsvFile() throws Exception {
        doReturn(fileReader).when(fileReaderGetter).get("import.csv");

        doReturn(new ArrayList<LocalKino>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }}).when(kinoImportCreator).getKinos(fileReader);

        new CsvImporter(fileReaderGetter, kinoImportCreator, localKinoRepository).importCsvFile();

        verify(localKinoRepository).createOrUpdate(new ArrayList<LocalKino>() {{
            add(aLocalKino);
            add(anotherLocalKino);
        }});
    }
}