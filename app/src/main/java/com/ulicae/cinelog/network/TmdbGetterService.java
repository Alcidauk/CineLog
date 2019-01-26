package com.ulicae.cinelog.network;

import android.content.Context;

import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.network.task.sync.SerieSyncNetworkTask;
import com.uwetrottmann.tmdb2.entities.TvShow;

import retrofit2.Call;

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
public class TmdbGetterService {

    private TmdbServiceWrapper tmdbServiceWrapper;

    public TmdbGetterService(Context context) {
        this(new TmdbServiceWrapper(context));
    }

    TmdbGetterService(TmdbServiceWrapper tmdbServiceWrapper) {
        this.tmdbServiceWrapper = tmdbServiceWrapper;
    }

    public void startSyncWithTmdb(SerieService serieService, SerieReview serieReview, long tmdbId) {
        Call<TvShow> tvShowCall = tmdbServiceWrapper.searchTvShowById(Long.valueOf(tmdbId).intValue());
        //noinspection unchecked
        new SerieSyncNetworkTask(new SerieBuilderFromMovie(), serieReview, serieService).execute(tvShowCall);
    }
}
