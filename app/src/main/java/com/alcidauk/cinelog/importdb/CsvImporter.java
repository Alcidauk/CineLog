package com.alcidauk.cinelog.importdb;

import android.app.Activity;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class CsvImporter {
    private final Activity activity;
    private FileReaderGetter fileReaderGetter;
    private final KinoImportCreator kinoImportCreator;
    private LocalKinoRepository localKinoRepository;

    public CsvImporter(ImportInDb importInDb, DaoSession daoSession) {
        this(importInDb, new FileReaderGetter(), new KinoImportCreator(), new LocalKinoRepository(daoSession));
    }

    public CsvImporter(Activity activity, FileReaderGetter fileReaderGetter, KinoImportCreator kinoImportCreator, LocalKinoRepository localKinoRepository) {
        this.activity = activity;
        this.fileReaderGetter = fileReaderGetter;
        this.kinoImportCreator = kinoImportCreator;
        this.localKinoRepository = localKinoRepository;
    }

    public void importCsvFile() throws ImportException {
        FileReader fileReader;
        try {
            fileReader = fileReaderGetter.get("import.csv");
        } catch (IOException e) {
            throw new ImportException("Can't open CSV file. It must be named import.csv and be placed at storage root directory", e);
        }
        List<LocalKino> kinos = kinoImportCreator.getKinos(fileReader);

        localKinoRepository.create(kinos);
    }

}
