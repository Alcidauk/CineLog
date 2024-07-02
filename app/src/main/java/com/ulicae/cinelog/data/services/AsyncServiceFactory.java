package com.ulicae.cinelog.data.services;

import android.content.Context;

import com.ulicae.cinelog.room.services.ReviewService;
import com.ulicae.cinelog.room.services.SerieReviewService;
import com.ulicae.cinelog.room.AppDatabase;

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

    private Context context;

    public AsyncServiceFactory(Context baseContext) {
        context = baseContext;
    }

    public RoomDataService create(String type, AppDatabase db) {
        switch (type) {
            case "kino":
                return new ReviewService(db);
            case "serie":
                return new SerieReviewService(db);
        }

        throw new NullPointerException("Unable to find a service for this type.");
    }
}
