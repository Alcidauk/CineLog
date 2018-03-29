package com.alcidauk.cinelog.importdb;

import com.alcidauk.cinelog.dao.DaoSession;
import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class CsvImporter {
    private FileReaderGetter fileReaderGetter;
    private final KinoImportCreator kinoImportCreator;
    private LocalKinoRepository localKinoRepository;

    @SuppressWarnings("WeakerAccess")
    public CsvImporter(DaoSession daoSession) {
        this(new FileReaderGetter(), new KinoImportCreator(), new LocalKinoRepository(daoSession));
    }

    CsvImporter(FileReaderGetter fileReaderGetter, KinoImportCreator kinoImportCreator, LocalKinoRepository localKinoRepository) {
        this.fileReaderGetter = fileReaderGetter;
        this.kinoImportCreator = kinoImportCreator;
        this.localKinoRepository = localKinoRepository;
    }

    @SuppressWarnings("WeakerAccess")
    public void importCsvFile() throws ImportException {
        FileReader fileReader;
        try {
            fileReader = fileReaderGetter.get("import.csv");
        } catch (IOException e) {
            throw new ImportException("Can't open CSV file. It must be named import.csv and be placed at storage root directory", e);
        }
        List<LocalKino> kinos = kinoImportCreator.getKinos(fileReader);

        localKinoRepository.createOrUpdate(kinos);
    }

}
