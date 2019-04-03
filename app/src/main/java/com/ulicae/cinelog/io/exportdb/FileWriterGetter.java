package com.ulicae.cinelog.io.exportdb;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import java.io.File;
import java.io.FileWriter;
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
public class FileWriterGetter {

    private FileUtilsWrapper fileUtilsWrapper;

    FileWriterGetter(){
        this.fileUtilsWrapper = new FileUtilsWrapper();
    }

    public FileWriter get(String name) throws IOException {
        return new FileWriter(new File(fileUtilsWrapper.getCineLogRoot(), name));
    }
}
