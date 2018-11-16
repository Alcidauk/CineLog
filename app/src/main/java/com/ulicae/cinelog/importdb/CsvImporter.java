package com.ulicae.cinelog.importdb;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.dao.DaoSession;
import com.ulicae.cinelog.dto.KinoDto;
import com.ulicae.cinelog.dto.KinoService;

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
class CsvImporter {
    private FileReaderGetter fileReaderGetter;
    private final KinoImportCreator kinoImportCreator;
    private KinoService kinoService;
    private Context context;

    @SuppressWarnings("WeakerAccess")
    public CsvImporter(DaoSession daoSession, Context context) {
        this(new FileReaderGetter(), new KinoImportCreator(context), new KinoService(daoSession), context);
    }

    CsvImporter(FileReaderGetter fileReaderGetter, KinoImportCreator kinoImportCreator, KinoService kinoService, Context context) {
        this.fileReaderGetter = fileReaderGetter;
        this.kinoImportCreator = kinoImportCreator;
        this.kinoService = kinoService;
        this.context = context;
    }

    @SuppressWarnings("WeakerAccess")
    public void importCsvFile() throws ImportException {
        FileReader fileReader;
        try {
            fileReader = fileReaderGetter.get("import.csv");
        } catch (IOException e) {
            throw new ImportException(context.getString(R.string.import_io_error_toast), e);
        }
        List<KinoDto> kinos = kinoImportCreator.getKinos(fileReader);

        kinoService.createOrUpdateKinos(kinos);
    }

}
