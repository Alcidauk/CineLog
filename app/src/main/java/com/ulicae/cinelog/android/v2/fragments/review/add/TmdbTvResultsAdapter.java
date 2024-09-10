package com.ulicae.cinelog.android.v2.fragments.review.add;


import android.content.Context;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;

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
public class TmdbTvResultsAdapter extends TmdbResultAdapter<BaseTvShow> {

    public TmdbTvResultsAdapter(Context context,
                                KinoApplication app,
                                List<BaseTvShow> results,
                                ReviewItemCallback reviewItemCallback,
                                ReviewCreationCallback reviewCreationCallback) {
        super(context,
                results,
                new ReviewAsyncService(app, ItemEntityType.SERIE),
                new SerieBuilderFromMovie(),
                reviewItemCallback, reviewCreationCallback);
    }
}
