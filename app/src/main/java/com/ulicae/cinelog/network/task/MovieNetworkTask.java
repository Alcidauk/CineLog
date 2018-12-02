package com.ulicae.cinelog.network.task;

import com.ulicae.cinelog.android.activities.add.AddKino;
import com.ulicae.cinelog.android.activities.add.AddReviewActivity;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.util.List;

import retrofit2.Response;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class MovieNetworkTask extends NetworkTask<MovieResultsPage, BaseMovie> {

    MovieNetworkTask(AddReviewActivity<Movie> addReviewActivity) {
        super(addReviewActivity);
    }

    @Override
    List<BaseMovie> getResults(Response<MovieResultsPage> response) {
        return response.body().results;
    }

    @Override
    void populateListView(List<BaseMovie> movies) {
        ((AddKino) getAddReviewActivityWeakReference().get()).populateListView(movies);
    }
}
