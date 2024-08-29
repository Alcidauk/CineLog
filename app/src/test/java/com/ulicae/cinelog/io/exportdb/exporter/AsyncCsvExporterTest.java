package com.ulicae.cinelog.io.exportdb.exporter;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.services.AsyncDataService;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;
import com.ulicae.cinelog.utils.ToasterWrapper;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;

@RunWith(MockitoJUnitRunner.class)
public class AsyncCsvExporterTest extends TestCase {

    @Mock
    AsyncDataService<DummyDto> service;

    @Mock
    CsvExportWriter<DummyDto> writer;

    @Mock
    ToasterWrapper toasterWrapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    public class DummyDto implements ItemDto {

        @Override
        public Long getId() {
            return 456L;
        }

        @Override
        public void setId(Long id) {
        }
    }

    @Test
    public void testExportSucceeded() throws IOException {

        DummyDto aDto = new DummyDto();
        doReturn(Flowable.just(new ArrayList() {{
            add(aDto);
        }})).when(service).findAll();


        new AsyncCsvExporter<>(service, writer, null).export();

        verify(writer).write(aDto);
        verify(writer).endWriting();
    }

    @Test
    public void testExportIoException() throws IOException {
        doThrow(IOException.class).when(writer).endWriting();

        DummyDto aDto = new DummyDto();
        Flowable observable = Flowable.just(new ArrayList() {{
            add(aDto);
        }});

        doReturn(observable).when(service).findAll();

        new AsyncCsvExporter<>(service, writer, toasterWrapper).export();

        verify(toasterWrapper).toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
    }
}