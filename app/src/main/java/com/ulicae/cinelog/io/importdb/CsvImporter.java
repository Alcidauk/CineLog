package com.ulicae.cinelog.io.importdb;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.DataService;
import com.ulicae.cinelog.data.dto.KinoDto;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
class CsvImporter<Dto extends KinoDto> {

    private FileReaderGetter fileReaderGetter;
    private final KinoImportCreator<Dto> kinoImportCreator;
    private DataService<Dto> dataService;
    private Context context;

    public CsvImporter(FileReaderGetter fileReaderGetter, KinoImportCreator<Dto> kinoImportCreator, DataService<Dto> dataService, Context context) {
        this.fileReaderGetter = fileReaderGetter;
        this.kinoImportCreator = kinoImportCreator;
        this.dataService = dataService;
        this.context = context;
    }

    @SuppressWarnings("WeakerAccess")
    public void importCsvFile(String importFilename) throws ImportException {
        FileReader fileReader;
        try {
            fileReader = fileReaderGetter.get(importFilename);
        } catch (IOException e) {
            throw new ImportException(context.getString(R.string.import_io_error_toast), e);
        }
        List<Dto> kinos = kinoImportCreator.getKinos(fileReader);

        dataService.createOrUpdateWithTmdbId(kinos);
    }

}
