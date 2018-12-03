package com.ulicae.cinelog.io.exportdb.exporter;

import java.io.FileWriter;
import java.io.IOException;

public interface ExporterFactory {

    CsvExporter makeCsvExporter(FileWriter fileWriter) throws IOException;

}
