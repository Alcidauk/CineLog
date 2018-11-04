package com.ulicae.cinelog.importdb;

import android.os.Environment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderGetter {

    public FileReader get(String name) throws IOException {
        File dataDirectory = Environment.getExternalStorageDirectory();

        File child = new File(dataDirectory, name);
        if (!child.exists()) {
            throw new IOException("Can't find import CSV file.");
        }

        return new FileReader(child);
    }
}
