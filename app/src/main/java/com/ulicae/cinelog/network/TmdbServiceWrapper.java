package com.ulicae.cinelog.network;


import android.content.Context;
import android.util.Log;

import com.ulicae.cinelog.utils.LocaleWrapper;
import com.ulicae.cinelog.utils.PreferencesWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.entities.TvEpisode;
import com.uwetrottmann.tmdb2.entities.TvSeason;
import com.uwetrottmann.tmdb2.entities.TvShow;
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage;
import com.uwetrottmann.tmdb2.services.TvService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
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
public class TmdbServiceWrapper {

    private Tmdb tmdb;

    private final Context context;
    private PreferencesWrapper preferencesWrapper;
    private LocaleWrapper localeWrapper;

    private static final String API_KEY = "da65d0969874404ac9ede3848f9c20ec";

    public TmdbServiceWrapper(Context context) {
        this(new Tmdb(API_KEY), context, new PreferencesWrapper(), new LocaleWrapper());
    }

    TmdbServiceWrapper(Tmdb tmdb, Context context, PreferencesWrapper preferencesWrapper, LocaleWrapper localeWrapper) {
        this.tmdb = tmdb;
        this.context = context;
        this.preferencesWrapper = preferencesWrapper;
        this.localeWrapper = localeWrapper;
    }

    public Call<MovieResultsPage> search(String name) {
        initSearchLanguageIfNeeded();

        return tmdb.searchService().movie(
                name,
                1,
                getTmdbPrefLanguage(),
                null,
                null,
                null,
                null
        );
    }

    public Call<TvShowResultsPage> searchTv(String name) {
        initSearchLanguageIfNeeded();

        return tmdb.searchService().tv(
                name,
                1,
                getTmdbPrefLanguage(),
                null
        );
    }

    public List<Integer> tvShowSeasons(int tvShowId) {
        TvService tvService = tmdb.tvService();

        try {
            Response<TvShow> response = tvService
                    .tv(tvShowId, getTmdbPrefLanguage())
                    .execute();
            if (response.isSuccessful()) {
                List<Integer> seasonNumbers = new ArrayList<>();
                List<TvSeason> seasons = response.body().seasons;
                for (TvSeason season : seasons) {
                    seasonNumbers.add(season.season_number);
                }
                return seasonNumbers;
            }
        } catch (Exception e) {
            Log.i("tmdbGetter", e.getMessage());
        }
        return null;
    }

    public List<TvEpisode> tvShowEpisodes(int tvShowId, List<Integer> tvSeasonNumbers) {
        List<TvEpisode> episodes = new ArrayList<>();
        for (Integer seasonNumber : tvSeasonNumbers) {
            try {
                Response<TvSeason> response = tmdb.tvSeasonsService()
                        .season(tvShowId, seasonNumber, getTmdbPrefLanguage())
                        .execute();
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().episodes != null) {
                        episodes.addAll(response.body().episodes);
                    }
                }
            } catch (Exception e) {
                // see execute() javadoc
            }
        }

        return episodes;
    }


    public Call<TvShow> searchTvShowById(int tvShowId) {
        initSearchLanguageIfNeeded();

        return tmdb.tvService().tv(tvShowId, getTmdbPrefLanguage());
    }

    void initSearchLanguageIfNeeded() {
        if (getTmdbPrefLanguage() == null) {
            preferencesWrapper.setStringPreference(context, "default_tmdb_language", localeWrapper.getLanguage());
        }
    }

    private String getTmdbPrefLanguage() {
        return preferencesWrapper.getStringPreference(context, "default_tmdb_language", null);
    }
}
