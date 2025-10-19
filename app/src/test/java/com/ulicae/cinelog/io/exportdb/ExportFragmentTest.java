package com.ulicae.cinelog.io.exportdb;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.ExporterFactory;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.utils.ToasterWrapper;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@RunWith(MockitoJUnitRunner.class)
public class ExportFragmentTest extends TestCase {

    @Mock
    private DocumentFile docFile;

    @Mock
    private ExporterFactory<? extends ItemDto> exporterFactory;

    @Mock
    private SnapshotExporterFactory snapshotExporterFactory;

    @Mock
    private SnapshotExporter<? extends ItemDto> snapshotExporter;

    @Mock
    private ToasterWrapper toasterWrapper;

    @Mock
    private CinelogSchedulers cinelogSchedulers;

    @Before
    public void before() {
        doReturn(Schedulers.trampoline()).when(cinelogSchedulers).androidMainThread();
    }

    @Test
    public void testExportForTypeNoDocumentFile() {
        ExportFragment exportFragment = getExportFragment();

        exportFragment.exportForType(docFile, "coucou", exporterFactory);

        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);
        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
    }

    @Test
    public void testExportForTypeExportIOException() throws IOException {
        DocumentFile childDocumentFile = mock(DocumentFile.class);
        doReturn(childDocumentFile).when(docFile).createFile("application/text", "coucou");

        Uri uri = mock(Uri.class);
        doReturn(uri).when(childDocumentFile).getUri();

        doReturn(snapshotExporter).when(snapshotExporterFactory).makeSnapshotExporter(exporterFactory);
        doThrow(new IOException()).when(snapshotExporter).export(uri);

        ExportFragment exportFragment = getExportFragment();

        exportFragment.exportForType(docFile, "coucou", exporterFactory);

        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);
        verify(toasterWrapper).toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
    }

    @Test
    public void testExportForTypeFlowableIOException() throws IOException {
        DocumentFile childDocumentFile = mock(DocumentFile.class);
        doReturn(childDocumentFile).when(docFile).createFile("application/text", "coucou");

        Uri uri = mock(Uri.class);
        doReturn(uri).when(childDocumentFile).getUri();

        doReturn(snapshotExporter).when(snapshotExporterFactory).makeSnapshotExporter(exporterFactory);

        Flowable<ArrayList<? extends ItemDto>> just = Flowable.error(new IOException());
        doReturn(just).when(snapshotExporter).export(uri);

        ExportFragment exportFragment = getExportFragment();

        exportFragment.exportForType(docFile, "coucou", exporterFactory);

        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);
        verify(toasterWrapper).toast(R.string.export_io_error_toast, ToasterWrapper.ToasterDuration.LONG);
    }

    @Test
    public void testExportForType() throws IOException {
        doReturn(snapshotExporter).when(snapshotExporterFactory).makeSnapshotExporter(exporterFactory);

        DocumentFile childDocumentFile = mock(DocumentFile.class);
        doReturn(childDocumentFile).when(docFile).createFile("application/text", "coucou");

        Uri uri = mock(Uri.class);
        doReturn(uri).when(childDocumentFile).getUri();

        Flowable<ArrayList<? extends ItemDto>> just = Flowable.just(new ArrayList<>());
        doReturn(just).when(snapshotExporter).export(any(Uri.class));

        ExportFragment exportFragment = getExportFragment();

        exportFragment.exportForType(docFile, "coucou", exporterFactory);

        just.test().assertComplete();

        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_start_toast, ToasterWrapper.ToasterDuration.SHORT);
        verify(toasterWrapper).toast(com.ulicae.cinelog.R.string.export_succeeded_toast, ToasterWrapper.ToasterDuration.LONG);
    }

    private @NonNull ExportFragment getExportFragment() {
        ExportFragment exportFragment = new ExportFragment();
        exportFragment.setToasterWrapper(toasterWrapper);
        exportFragment.setSnapshotExporterFactory(snapshotExporterFactory);
        exportFragment.setDisposableList(new ArrayList<>());
        exportFragment.setCinelogSchedulers(cinelogSchedulers);
        return exportFragment;
    }

}