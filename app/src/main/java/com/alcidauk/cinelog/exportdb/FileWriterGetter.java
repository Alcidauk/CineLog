package com.alcidauk.cinelog.exportdb;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterGetter {

    public FileWriter get(String name) throws IOException {
        File dataDirectory = Environment.getExternalStorageDirectory();

        return new FileWriter(new File(dataDirectory, name));
    }
}
