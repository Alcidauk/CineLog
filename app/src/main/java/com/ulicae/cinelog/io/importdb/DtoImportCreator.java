package com.ulicae.cinelog.io.importdb;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.io.importdb.builder.DtoFromRecordBuilder;

import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
class DtoImportCreator<Dto> {
    private CSVFormatWrapper csvFormatWrapper;
    private DtoFromRecordBuilder<Dto> dtoFromRecordBuilder;
    private Context context;

    DtoImportCreator(Context context, DtoFromRecordBuilder<Dto> builder) {
        this(new CSVFormatWrapper(), builder, context);
    }

    DtoImportCreator(CSVFormatWrapper csvFormatWrapper, DtoFromRecordBuilder<Dto> dtoFromRecordBuilder, Context context) {
        this.csvFormatWrapper = csvFormatWrapper;
        this.dtoFromRecordBuilder = dtoFromRecordBuilder;
        this.context = context;
    }

    List<Dto> getDtos(FileReader fileReader) throws ImportException {
        Iterable<CSVRecord> csvRecords;
        try {
            csvRecords = csvFormatWrapper.parse(fileReader);
        } catch (IOException e) {
            throw new ImportException(context.getString(R.string.import_parsing_error_toast), e);
        }

        List<Dto> kinos = new ArrayList<>();
        for (CSVRecord csvRecord : csvRecords) {
            kinos.add(dtoFromRecordBuilder.build(csvRecord));
        }

        return kinos;
    }

}
