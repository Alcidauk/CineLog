package com.ulicae.cinelog.android.activities.add;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.android.v2.fragments.MovieDetailsCallback;
import com.ulicae.cinelog.android.v2.fragments.MovieReviewCreationCallback;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;

import org.parceler.Parcels;

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
public class TvResultsAdapter extends ItemResultAdapter<BaseTvShow> {

    public TvResultsAdapter(Context context,
                            KinoApplication app,
                            List<BaseTvShow> results,
                            MovieDetailsCallback movieDetailsCallback,
                            MovieReviewCreationCallback movieReviewCreationCallback) {
        super(context,
                results,
                new SerieService(app.getDaoSession(), context),
                new SerieBuilderFromMovie(),
                movieDetailsCallback, movieReviewCreationCallback);
    }
}
