package com.ulicae.cinelog.io.exportdb;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;

@RunWith(MockitoJUnitRunner.class)
public class SnapshotExporterTest extends TestCase {

    @Mock
    private ExporterFactory<? extends ItemDto> exporterFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ContentResolver contentResolver;

    @Mock
    private Uri uri;

    private static class DummyDtoTestCase implements ItemDto {

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {

        }
    }
    @Test
    public void testExportSucceeded() throws IOException {
        CsvExporter<? extends ItemDto> csvExporter = mock(CsvExporter.class);

        Flowable<ArrayList<? extends ItemDto>> flowable = Flowable.just(new ArrayList<ItemDto>() {{
            add(new DummyDtoTestCase());
        }});
        doReturn(flowable).when(csvExporter).export();

        ParcelFileDescriptor pfd = mock(ParcelFileDescriptor.class);
        FileDescriptor fileDescriptor = mock(FileDescriptor.class);
        doReturn(fileDescriptor).when(pfd).getFileDescriptor();
        doReturn(pfd).when(contentResolver).openFileDescriptor(uri, "w");

        doReturn(csvExporter).when(exporterFactory).makeCsvExporter(any(FileWriter.class));

        new SnapshotExporter<>(exporterFactory, contentResolver)
                .export(uri)
                .test()
                .assertComplete();
    }

    @Test
    public void testExportFailedErrorOnSubscribe() throws IOException {
        CsvExporter<? extends ItemDto> csvExporter = mock(CsvExporter.class);

        Flowable<ArrayList<? extends ItemDto>> flowable = Flowable.error(new IOException());
        doReturn(flowable).when(csvExporter).export();

        ParcelFileDescriptor pfd = mock(ParcelFileDescriptor.class);
        FileDescriptor fileDescriptor = mock(FileDescriptor.class);
        doReturn(fileDescriptor).when(pfd).getFileDescriptor();
        doReturn(pfd).when(contentResolver).openFileDescriptor(uri, "w");

        doReturn(csvExporter).when(exporterFactory).makeCsvExporter(any(FileWriter.class));

        new SnapshotExporter<>(exporterFactory, contentResolver)
                .export(uri)
                .test()
                .assertError(IOException.class);
    }

    @Test(expected = IOException.class)
    public void testExportNullParcelFileDescriptor() throws IOException {
        new SnapshotExporter<>(exporterFactory, contentResolver).export(uri);
    }

    @Test(expected = IOException.class)
    public void testExportIoException() throws IOException {
        ParcelFileDescriptor pfd = mock(ParcelFileDescriptor.class);
        FileDescriptor fileDescriptor = mock(FileDescriptor.class);
        doReturn(fileDescriptor).when(pfd).getFileDescriptor();

        doReturn(pfd).when(contentResolver).openFileDescriptor(uri, "w");
        doThrow(new IOException()).when(exporterFactory).makeCsvExporter(any(FileWriter.class));

        new SnapshotExporter<>(exporterFactory, contentResolver).export(uri);
    }

}