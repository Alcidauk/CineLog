package com.alcidauk.cinelog.export;

import com.opencsv.bean.StatefulBeanToCsvBuilder;



public class CsvBuilderWrapper {

    private FileWriterGetter fileWriterGetter;

    public CsvBuilderWrapper(FileWriterGetter fileWriterGetter) {
        this.fileWriterGetter = fileWriterGetter;
    }

    public StatefulBeanToCsvBuilder getCsvBuilder() {
        return new StatefulBeanToCsvBuilder(fileWriterGetter.get("/path/to/file"));
    }
}
