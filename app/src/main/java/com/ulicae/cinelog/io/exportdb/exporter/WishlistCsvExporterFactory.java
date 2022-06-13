package com.ulicae.cinelog.io.exportdb.exporter;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.wishlist.WishlistService;
import com.ulicae.cinelog.io.exportdb.writer.WishlistCsvExportWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * CineLog Copyright 2020 Pierre Rognon
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
public class WishlistCsvExporterFactory implements ExporterFactory{

    private final WishlistService wishlistService;

    public WishlistCsvExporterFactory(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    public CsvExporter<WishlistDataDto> makeCsvExporter(FileWriter fileWriter) throws IOException {
        return new CsvExporter<>(wishlistService, new WishlistCsvExportWriter(fileWriter));
    }
}
