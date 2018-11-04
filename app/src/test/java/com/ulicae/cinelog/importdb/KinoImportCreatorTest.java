package com.ulicae.cinelog.importdb;

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

    @Test
    public void getKinos() throws Exception {
        doReturn(new ArrayList<CSVRecord>() {{
            add(record);
        }}).when(csvFormatWrapper).parse(fileReader);

        doReturn(localKino).when(localKinoBuilder).build(record);

        assertEquals(
                Collections.singletonList(localKino),
                new KinoImportCreator(csvFormatWrapper, localKinoBuilder).getKinos(fileReader)
        );
    }

    @Test(expected = ImportException.class)
    public void getKinos_ioException_onWrapper() throws Exception {
        doThrow(new IOException()).when(csvFormatWrapper).parse(fileReader);

        assertEquals(
                Collections.singletonList(localKino),
                new KinoImportCreator(csvFormatWrapper, localKinoBuilder).getKinos(fileReader)
        );
    }

}