package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class AsyncServiceFactory {

    // TODO is this class needed
    public AsyncDataService<KinoDto> create(String type, KinoApplication app) {
        switch (type) {
            case "kino":
                return new ReviewAsyncService(app, ItemEntityType.MOVIE);
            case "serie":
                return new ReviewAsyncService(app, ItemEntityType.SERIE);
        }

        throw new NullPointerException("Unable to find a service for this type.");
    }
}
