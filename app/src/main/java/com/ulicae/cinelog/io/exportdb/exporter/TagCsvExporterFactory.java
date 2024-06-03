package com.ulicae.cinelog.io.exportdb.exporter;

import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.room.TagAsyncService;
import com.ulicae.cinelog.io.exportdb.writer.TagCsvExportWriter;
import com.ulicae.cinelog.room.AppDatabase;

import java.io.FileWriter;
import java.io.IOException;

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
public class TagCsvExporterFactory implements ExporterFactory {

    private final TagAsyncService tagService;

    public TagCsvExporterFactory(AppDatabase db) {
        this(new TagAsyncService(db));
    }

    private TagCsvExporterFactory(TagAsyncService tagService) {
        this.tagService = tagService;
    }

    public CsvExporter<TagDto> makeCsvExporter(FileWriter fileWriter) throws IOException {
        return new CsvExporter<>(tagService, new TagCsvExportWriter(fileWriter));
    }
}
