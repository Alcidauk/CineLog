package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExportTreeManagerTest {

    @Mock
    private FileUtilsWrapper fileUtilsWrapper;

    @Mock
    private File externalRoot;

    @Test
    public void prepareTreeCreateDirs() throws IOException {
        doReturn(externalRoot).when(fileUtilsWrapper).getExternalStorageDirectory();
        doReturn("/root").when(externalRoot).getAbsolutePath();

        File cinelogSaves = mock(File.class);
        doReturn(cinelogSaves).when(fileUtilsWrapper).getFile("/root/CineLog/saves");

        //noinspection ResultOfMethodCallIgnored
        doReturn(false).when(cinelogSaves).exists();

        new ExportTreeManager(fileUtilsWrapper).prepareTree();

        //noinspection ResultOfMethodCallIgnored
        verify(cinelogSaves).mkdirs();
    }

    @Test
    public void prepareTreeDoNotCreateDirs() throws IOException {
        doReturn(externalRoot).when(fileUtilsWrapper).getExternalStorageDirectory();
        doReturn("/root").when(externalRoot).getAbsolutePath();

        File cinelogSaves = mock(File.class);
        doReturn(cinelogSaves).when(fileUtilsWrapper).getFile("/root/CineLog/saves");

        //noinspection ResultOfMethodCallIgnored
        doReturn(true).when(cinelogSaves).exists();

        new ExportTreeManager(fileUtilsWrapper).prepareTree();

        //noinspection ResultOfMethodCallIgnored
        verify(cinelogSaves, never()).mkdirs();
    }

    @Test
    public void isExportNeededTrue() {
        doReturn(externalRoot).when(fileUtilsWrapper).getExternalStorageDirectory();
        doReturn("/root").when(externalRoot).getAbsolutePath();

        File file = mock(File.class);
        String path = "/root/CineLog/saves/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        doReturn(file).when(fileUtilsWrapper).getFile(path);

        doReturn(false).when(file).exists();

        assertTrue(new ExportTreeManager(fileUtilsWrapper).isExportNeeded());
    }

    @Test
    public void isExportNeededFalse() {
        doReturn(externalRoot).when(fileUtilsWrapper).getExternalStorageDirectory();
        doReturn("/root").when(externalRoot).getAbsolutePath();

        File file = mock(File.class);
        String path = "/root/CineLog/saves/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        doReturn(file).when(fileUtilsWrapper).getFile(path);

        doReturn(true).when(file).exists();

        assertFalse(new ExportTreeManager(fileUtilsWrapper).isExportNeeded());
    }

    @Test
    public void getNextExportFile() throws IOException {
        doReturn(externalRoot).when(fileUtilsWrapper).getExternalStorageDirectory();
        doReturn("/root").when(externalRoot).getAbsolutePath();

        String path = "/root/CineLog/saves/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(fileUtilsWrapper).getFileWriter(new File(path));

        assertEquals(
                fileWriter,
                new ExportTreeManager(fileUtilsWrapper).getNextExportFile()
        );
    }
}