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
    public void prepareTreeCreateDirs() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        //noinspection ResultOfMethodCallIgnored
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File movieCinelogSaves = mock(File.class);
        doReturn(movieCinelogSaves).when(fileUtilsWrapper).getFile("/root/CineLog/saves/movie");

        //noinspection ResultOfMethodCallIgnored
        doReturn(false).when(movieCinelogSaves).exists();

        new ExportTreeManager(fileUtilsWrapper, "movie").prepareTree();

        //noinspection ResultOfMethodCallIgnored
        verify(movieCinelogSaves).mkdirs();
    }

    @Test
    public void prepareTreeDoNotCreate() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        //noinspection ResultOfMethodCallIgnored
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File movieCinelogSaves = mock(File.class);
        doReturn(movieCinelogSaves).when(fileUtilsWrapper).getFile("/root/CineLog/saves/movie");

        //noinspection ResultOfMethodCallIgnored
        doReturn(true).when(movieCinelogSaves).exists();

        new ExportTreeManager(fileUtilsWrapper, "movie").prepareTree();

        //noinspection ResultOfMethodCallIgnored
        verify(movieCinelogSaves, never()).mkdirs();
    }

    @Test
    public void isExportNeededTrue() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        //noinspection ResultOfMethodCallIgnored
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File file = mock(File.class);
        String path = "/root/CineLog/saves/movie/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        doReturn(file).when(fileUtilsWrapper).getFile(path);

        //noinspection ResultOfMethodCallIgnored
        doReturn(false).when(file).exists();

        assertTrue(new ExportTreeManager(fileUtilsWrapper, "movie").isExportNeeded());
    }

    @Test
    public void isExportNeededFalse() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File file = mock(File.class);
        String path = "/root/CineLog/saves/movie/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        doReturn(file).when(fileUtilsWrapper).getFile(path);

        doReturn(true).when(file).exists();

        assertFalse(new ExportTreeManager(fileUtilsWrapper, "movie").isExportNeeded());
    }

    @Test
    public void getNextExportFile() throws IOException {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        String path = "/root/CineLog/saves/movie/export" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        FileWriter fileWriter = mock(FileWriter.class);
        doReturn(fileWriter).when(fileUtilsWrapper).getFileWriter(new File(path));

        assertEquals(
                fileWriter,
                new ExportTreeManager(fileUtilsWrapper, "movie").getNextExportFile()
        );
    }

    @Test
    public void clean() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File saveRoot = mock(File.class);
        doReturn(saveRoot).when(fileUtilsWrapper).getFile("/root/CineLog/saves/movie");

        File fileToRemove = mockFile(5L);
        //noinspection ResultOfMethodCallIgnored
        doReturn(new File[]{
                fileToRemove,
                mockFile(10L),
                mockFile(24L),
                mockFile(321L),
                mockFile(4444444L),
                mockFile(45L),
                mockFile(456456L),
                mockFile(1231321L),
                mockFile(789453L),
                mockFile(7777L),
                mockFile(987987L)
        }).when(saveRoot).listFiles();

        new ExportTreeManager(fileUtilsWrapper, "movie").clean();

        //noinspection ResultOfMethodCallIgnored
        verify(fileToRemove).delete();
    }

    @Test
    public void cleanNotEnoughFiles() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File saveRoot = mock(File.class);
        doReturn(saveRoot).when(fileUtilsWrapper).getFile("/root/CineLog/saves/movie");

        File saveFile1 = mockFile(5L);
        File saveFile2 = mockFile(5L);
        //noinspection ResultOfMethodCallIgnored
        doReturn(new File[]{
                saveFile1,
                saveFile2,
        }).when(saveRoot).listFiles();

        new ExportTreeManager(fileUtilsWrapper, "movie").clean();

        //noinspection ResultOfMethodCallIgnored
        verify(saveFile1, never()).delete();
        //noinspection ResultOfMethodCallIgnored
        verify(saveFile2, never()).delete();
    }

    @Test
    public void cleanNoFiles() {
        doReturn(externalRoot).when(fileUtilsWrapper).getFilesDir();
        doReturn("/root/CineLog").when(externalRoot).getAbsolutePath();

        File saveRoot = mock(File.class);
        doReturn(saveRoot).when(fileUtilsWrapper).getFile("/root/CineLog/saves/movie");

        //noinspection ResultOfMethodCallIgnored
        doReturn(new File[]{}).when(saveRoot).listFiles();

        new ExportTreeManager(fileUtilsWrapper, "movie").clean();
    }

    private File mockFile(long lastModified) {
        File file = mock(File.class);
        //noinspection ResultOfMethodCallIgnored
        doReturn(lastModified).when(file).lastModified();
        return file;
    }
}