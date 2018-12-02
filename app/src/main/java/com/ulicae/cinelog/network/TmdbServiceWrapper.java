package com.ulicae.cinelog.network;


import android.content.Context;

import com.ulicae.cinelog.utils.PreferencesWrapper;
import com.ulicae.cinelog.utils.LocaleWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage;

import retrofit2.Call;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
                "ngram"
        );
    }

    public Call<TvShowResultsPage> searchTv(String name) {
        initSearchLanguageIfNeeded();

        return tmdb.searchService().tv(
                name,
                1,
                getTmdbPrefLanguage(),
                null,
                null
        );    }


    void initSearchLanguageIfNeeded() {
        if(getTmdbPrefLanguage() == null){
            preferencesWrapper.setStringPreference(context,"default_tmdb_language", localeWrapper.getLanguage());
        }
    }

    private String getTmdbPrefLanguage() {
        return preferencesWrapper.getStringPreference(context, "default_tmdb_language", null);
    }
}
