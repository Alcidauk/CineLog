package com.ulicae.cinelog.exportdb;

import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.db.LocalKinoRepository;

import java.io.IOException;
import java.util.List;

public class CsvExporter {
    private LocalKinoRepository localKinoRepository;
    private final CsvExportWriter csvExportWriter;

    public CsvExporter(LocalKinoRepository localKinoRepository, Appendable out) throws IOException {
        this(localKinoRepository, new CsvExportWriter(out));
    }

    CsvExporter(LocalKinoRepository localKinoRepository, CsvExportWriter csvExportWriter) {
        this.localKinoRepository = localKinoRepository;
        this.csvExportWriter = csvExportWriter;
    }

    public void export() throws IOException {
        List<LocalKino> localKinoList = localKinoRepository.findAll();

        for (LocalKino localKino : localKinoList) {
            csvExportWriter.write(localKino);
        }

        csvExportWriter.endWriting();
    }
}
