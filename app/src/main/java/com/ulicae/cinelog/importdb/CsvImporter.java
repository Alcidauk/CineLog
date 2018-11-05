package com.ulicae.cinelog.importdb;

import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dao.LocalKino;
import com.ulicae.cinelog.db.LocalKinoRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
