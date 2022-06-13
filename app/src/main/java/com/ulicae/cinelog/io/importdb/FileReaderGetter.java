package com.ulicae.cinelog.io.importdb;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class FileReaderGetter {

    private final FileUtilsWrapper fileUtilsWrapper;

    FileReaderGetter(File dataDir){
        this.fileUtilsWrapper = new FileUtilsWrapper(dataDir);
    }

    public FileReader get(String name) throws IOException {
        File child = new File(fileUtilsWrapper.getCineLogRoot(), name);
        if (!child.exists()) {
            throw new IOException();
        }

        return new FileReader(child);
    }
}
