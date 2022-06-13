package com.ulicae.cinelog.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
public class FileUtilsWrapper {

    private File dataDir;

    public FileUtilsWrapper(File dataDir) {
        this.dataDir = dataDir;
    }

    public File getFilesDir() {
        return getAndCreateFile(dataDir.getAbsolutePath());
    }

    public File getFile(String path) {
        return new File(path);
    }

    public FileWriter getFileWriter(File file) throws IOException {
        return new FileWriter(file);
    }

    private File getAndCreateFile(String path){
        File file = new File(path);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        return file;
    }
}
