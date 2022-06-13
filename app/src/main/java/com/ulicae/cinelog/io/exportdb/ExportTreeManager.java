package com.ulicae.cinelog.io.exportdb;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
class ExportTreeManager {

    private FileUtilsWrapper fileUtilsWrapper;
    private String subDir;

    ExportTreeManager(File dataDir, String subDir) {
        this(new FileUtilsWrapper(dataDir), subDir);
    }

    ExportTreeManager(FileUtilsWrapper fileUtilsWrapper, String subDir) {
        this.fileUtilsWrapper = fileUtilsWrapper;
        this.subDir = subDir;
    }

    void prepareTree() {
        File root = fileUtilsWrapper.getFilesDir();

        createIfNotExist(String.format("%s/saves/%s", root.getAbsolutePath(), subDir));
    }

    private void createIfNotExist(String path) {
        File file = fileUtilsWrapper.getFile(path);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
    }

    boolean isExportNeeded() {
        return !fileUtilsWrapper.getFile(buildExportFilePath(subDir)).exists();
    }

    FileWriter getNextExportFile() throws IOException {
        return fileUtilsWrapper.getFileWriter(new File(buildExportFilePath(subDir)));
    }

    void clean() {
        File root = fileUtilsWrapper.getFilesDir();

        File saveRoot = fileUtilsWrapper.getFile(root.getAbsolutePath() + "/saves/" + subDir);

        File[] saveFiles = saveRoot.listFiles();
        if(saveFiles.length > 10){
            File oldestFile = null;
            for (File saveFile : saveFiles) {
                if(oldestFile == null || oldestFile.lastModified() > saveFile.lastModified()){
                    oldestFile = saveFile;
                }
            }

            if(oldestFile != null){
                //noinspection ResultOfMethodCallIgnored
                oldestFile.delete();
            }
        }
    }

    @NonNull
    private String buildExportFilePath(String subDir) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat todayDate = new SimpleDateFormat("yyyyMMdd");
        return String.format("%s/saves/%s/export%s.csv",
                fileUtilsWrapper.getFilesDir().getAbsolutePath(),
                subDir,
                todayDate.format(new Date())
        );
    }
}
