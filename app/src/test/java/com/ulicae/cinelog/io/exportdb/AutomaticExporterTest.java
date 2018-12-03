package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.exportdb.exporter.CsvExporter;
import com.ulicae.cinelog.io.exportdb.exporter.MovieCsvExporterFactory;
import com.ulicae.cinelog.utils.BusinessPreferenceGetter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.hasProperty;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticExporterTest {

    @Mock
    private ExportTreeManager exportTreeManager;

    @Mock
    private BusinessPreferenceGetter businessPreferenceGetter;

    @Mock
    private MovieCsvExporterFactory csvExporterFactory;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void tryExportNotEnabled() throws AutomaticExportException {
        doReturn(false).when(businessPreferenceGetter).getAutomaticExport();

        assertFalse(new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport());

        verify(exportTreeManager, never()).prepareTree();
    }

    @Test
    public void tryExportNotNeeded() throws IOException, AutomaticExportException {
        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(false).when(exportTreeManager).isExportNeeded();

        assertFalse(new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport());

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
        expectedException.expect(AutomaticExportException.class);
        expectedException.expect(hasProperty("stringCode", is(R.string.automatic_export_cant_export)));

        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(true).when(exportTreeManager).isExportNeeded();

        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(exportTreeManager).getNextExportFile();

        CsvExporter csvExporter = mock(CsvExporter.class);
        doReturn(csvExporter).when(csvExporterFactory).makeCsvExporter(fileWriter);

        doThrow(IOException.class).when(csvExporter).export();

        assertTrue(new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport());

        verify(exportTreeManager, never()).clean();
    }

    @Test
    public void tryExport() throws IOException, AutomaticExportException {
        doReturn(true).when(businessPreferenceGetter).getAutomaticExport();
        doReturn(true).when(exportTreeManager).isExportNeeded();

        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(exportTreeManager).getNextExportFile();

        CsvExporter csvExporter = mock(CsvExporter.class);
        doReturn(csvExporter).when(csvExporterFactory).makeCsvExporter(fileWriter);

        assertTrue(new AutomaticExporter(exportTreeManager, businessPreferenceGetter, csvExporterFactory).tryExport());

        verify(exportTreeManager).prepareTree();
        verify(csvExporter).export();
        verify(exportTreeManager).clean();
    }
}