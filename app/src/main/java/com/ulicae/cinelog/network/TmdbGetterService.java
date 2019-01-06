package com.ulicae.cinelog.network;

import android.content.Context;
import android.util.Log;

import com.ulicae.cinelog.data.dto.SerieDto;
import com.uwetrottmann.tmdb2.entities.TvShow;

import java.io.IOException;

public class TmdbGetterService {

    private TmdbServiceWrapper tmdbServiceWrapper;
    private SerieBuilderFromMovie serieBuilderFromMovie;

    public TmdbGetterService(Context context) {
        this(new TmdbServiceWrapper(context), new SerieBuilderFromMovie());
    }

    TmdbGetterService(TmdbServiceWrapper tmdbServiceWrapper, SerieBuilderFromMovie serieBuilderFromMovie) {
        this.tmdbServiceWrapper = tmdbServiceWrapper;
        this.serieBuilderFromMovie = serieBuilderFromMovie;
    }

    public SerieDto getSerieWithTmdbId(int tmdbId) {
        try {
            TvShow tvShow = tmdbServiceWrapper.searchTvShowById(tmdbId).execute().body();
            return serieBuilderFromMovie.build(tvShow);
        } catch (IOException e) {
            // TODO improve it
            Log.i("tmdbGetter", "Unable to execute task.");
            return null;
        }
    }
}
