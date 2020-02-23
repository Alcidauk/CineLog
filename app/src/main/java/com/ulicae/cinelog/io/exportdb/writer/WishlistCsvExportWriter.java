package com.ulicae.cinelog.io.exportdb.writer;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

import java.io.IOException;

/**
 * CineLog Copyright 2020 Pierre Rognon
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
public class WishlistCsvExportWriter extends CsvExportWriter<WishlistDataDto> {

    public enum Headers {
        id,
        movie_id,
        title,
        overview,
        firstYear,
        poster_path,
        release_date,
        wishlistItemType,
    }

    public WishlistCsvExportWriter(Appendable out) throws IOException {
        super(out, Headers.class);
    }

    WishlistCsvExportWriter(CSVPrinterWrapper csvPrinterWrapper) {
        super(csvPrinterWrapper);
    }

    public void write(WishlistDataDto wishlistDataDto) throws IOException {
        csvPrinterWrapper.printRecord(
                wishlistDataDto.getId(),
                wishlistDataDto.getTmdbId(),
                wishlistDataDto.getTitle(),
                wishlistDataDto.getOverview(),
                wishlistDataDto.getFirstYear(),
                wishlistDataDto.getPosterPath(),
                wishlistDataDto.getReleaseDate(),
                wishlistDataDto.getWishlistItemType().toString()
        );
    }
}
