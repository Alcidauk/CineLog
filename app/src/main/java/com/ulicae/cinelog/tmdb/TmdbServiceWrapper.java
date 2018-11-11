package com.ulicae.cinelog.tmdb;


import android.content.Context;

import com.ulicae.cinelog.PreferencesWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;

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

    private static final String API_KEY = "da65d0969874404ac9ede3848f9c20ec";

    public TmdbServiceWrapper(Context context) {
        this(new Tmdb(API_KEY), context, new PreferencesWrapper());
    }

    TmdbServiceWrapper(Tmdb tmdb, Context context, PreferencesWrapper preferencesWrapper) {
        this.tmdb = tmdb;
        this.context = context;
        this.preferencesWrapper = preferencesWrapper;
    }

    public Call<MovieResultsPage> search(String name) {
        return tmdb.searchService().movie(
                name,
                1,
                preferencesWrapper.getStringPreference(context, "default_tmdb_language", "en"),
                null,
                null,
                null,
                "ngram"
        );
    }
}
