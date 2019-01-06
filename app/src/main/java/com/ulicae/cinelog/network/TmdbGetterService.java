package com.ulicae.cinelog.network;

import android.content.Context;

import com.ulicae.cinelog.data.SerieService;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.network.task.sync.SerieSyncNetworkTask;
import com.uwetrottmann.tmdb2.entities.TvShow;

import retrofit2.Call;

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
