package com.alcidauk.cinelog.exportdb;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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