package com.alcidauk.cinelog.export;

import com.alcidauk.cinelog.dao.LocalKino;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CsvExportWriterTest {

    @Mock
    private CSVPrinterWrapper csvPrinterWrapper;

    @Mock
    private LocalKino aKino;

    @Test
    public void write() throws Exception {
        new CsvExportWriter(csvPrinterWrapper).write(aKino);

        verify(csvPrinterWrapper).printRecord(aKino.getId(), aKino.getMovie_id(), aKino.getOverview());
    }

    @Test
    public void endWriting() throws Exception {
        new CsvExportWriter(csvPrinterWrapper).endWriting();

        verify(csvPrinterWrapper).flush();
        verify(csvPrinterWrapper).close();
    }
}