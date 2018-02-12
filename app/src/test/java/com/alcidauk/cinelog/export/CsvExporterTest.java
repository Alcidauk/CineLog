package com.alcidauk.cinelog.export;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CsvExporterTest {

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Test
    public void export() throws Exception {
        final LocalKino aKino = mock(LocalKino.class);
        ArrayList<LocalKino> kinos = new ArrayList<LocalKino>() {{
            add(aKino);
        }};
        doReturn(kinos).when(localKinoRepository).findAll();

        new CsvExporter(localKinoRepository).export();

        // TODO make the test doing something...
    }
}