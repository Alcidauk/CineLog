package com.alcidauk.cinelog.export;

import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CsvBuilderWrapperTest {

    @Mock
    private FileWriterGetter fileWriterGetter;

    @Mock
    private FileWriter fileWriter;

    @Test
    public void getCsvBuilder() throws Exception {
        doReturn(fileWriter).when(fileWriterGetter).get("path/to/file");

        assertSame(
                new StatefulBeanToCsvBuilder<>(fileWriter),
                new CsvBuilderWrapper(fileWriterGetter).getCsvBuilder()
        );

    }
}