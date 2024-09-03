package com.ulicae.cinelog.io.exportdb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.io.exportdb.exporter.AsyncCsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.ReviewCsvExporterFactory;
import com.ulicae.cinelog.utils.BusinessPreferenceGetter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticExporterTest {

    @Mock
    private ExportTreeManager exportTreeManager;

    @Mock
    private BusinessPreferenceGetter businessPreferenceGetter;

    @Mock
    private ReviewCsvExporterFactory csvExporterFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    static class DummyDto implements ItemDto {
        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {

        }
    }

    @Test
    public void tryExportNotEnabled() throws AutomaticExportException {
        doReturn(false).when(businessPreferenceGetter).getAutomaticExport();

        new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport();

        verify(exportTreeManager, never()).prepareTree();
    }

    @Test
    public void tryExportNotNeeded() throws IOException, AutomaticExportException {
        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(false).when(exportTreeManager).isExportNeeded();

        new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport();

        verify(exportTreeManager).prepareTree();
        verify(exportTreeManager, never()).getNextExportFile();
    }

    @Test
    public void tryExportIOWhenGetNextExportFile() throws IOException, AutomaticExportException {
        expectedException.expect(AutomaticExportException.class);
        expectedException.expect(hasProperty("stringCode", is(R.string.automatic_export_cant_get_next_export)));

        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(true).when(exportTreeManager).isExportNeeded();

        doThrow(IOException.class).when(exportTreeManager).getNextExportFile();

        new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport();
    }

    @Test
    public void tryExportIOWhenExport() throws IOException, AutomaticExportException {
        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(true).when(exportTreeManager).isExportNeeded();

        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(exportTreeManager).getNextExportFile();

        AsyncCsvExporter asyncCsvExporter = mock(AsyncCsvExporter.class);
        doReturn(asyncCsvExporter).when(csvExporterFactory).makeCsvExporter(fileWriter);

        IOException throwable = new IOException();
        doReturn(Flowable.error(throwable)).when(asyncCsvExporter).export();

        TestSubscriber<List<DummyDto>> test =
                new AutomaticExporter<DummyDto>(
                        exportTreeManager,
                        businessPreferenceGetter,
                        csvExporterFactory)
                        .tryExport()
                        .test();

        test.assertError(throwable);

        verify(exportTreeManager, never()).clean();
    }

    @Test
    public void tryExport() throws IOException, AutomaticExportException {
        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(true).when(exportTreeManager).isExportNeeded();

        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(exportTreeManager).getNextExportFile();

        AsyncCsvExporter<DummyDto> asyncCsvExporter = mock(AsyncCsvExporter.class);
        doReturn(asyncCsvExporter).when(csvExporterFactory).makeCsvExporter(any(FileWriter.class));

        doReturn(Flowable.just(new ArrayList<>())).when(asyncCsvExporter).export();

        TestSubscriber<List<DummyDto>> test =
                new AutomaticExporter<DummyDto>(
                        exportTreeManager,
                        businessPreferenceGetter,
                        csvExporterFactory)
                        .tryExport()
                        .test();

        verify(exportTreeManager).prepareTree();
        verify(asyncCsvExporter).export();
        verify(exportTreeManager).clean();

        test.assertComplete();
    }


}