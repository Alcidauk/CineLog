package com.ulicae.cinelog.android.v2.fragments.review.add;

import android.content.Context;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

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
public class TmdbMovieResultsAdapter extends TmdbResultAdapter<BaseMovie> {

    public TmdbMovieResultsAdapter(Context context, KinoApplication app, List<BaseMovie> results,
                                   ReviewItemCallback reviewItemCallback,
                                   ReviewCreationCallback reviewCreationCallback) {
        super(context,
                results,
                new KinoService(app.getDaoSession()),
                new KinoBuilderFromMovie(),
                reviewItemCallback,
                reviewCreationCallback);
    }

}
