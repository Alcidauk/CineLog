package com.ulicae.cinelog.io.exportdb.exporter;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.room.services.AsyncDataService;
import com.ulicae.cinelog.io.exportdb.writer.CsvExportWriter;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class AsyncCsvExporterTest extends TestCase {

    @Mock
    AsyncDataService<DummyDto> service;

    @Mock
    CsvExportWriter<DummyDto> writer;

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

        TestSubscriber<List<DummyDto>> test = new AsyncCsvExporter<>(service, writer)
                .export()
                .test();

        test.assertComplete();

        verify(writer).write(aDto);
        verify(writer).endWriting();
    }

}