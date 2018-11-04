package com.ulicae.cinelog.tmdb;


import android.content.Context;

import com.ulicae.cinelog.PreferencesWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;

import retrofit2.Call;

public class TmdbServiceWrapper {


    private SearchService searchService;
    private final Context context;
    private PreferencesWrapper preferencesWrapper;

    private static final String API_KEY = "d6d6579b3a02efda2efde4585120d45e";

    public TmdbServiceWrapper(Context context) {
        this(new Tmdb(API_KEY), context, new PreferencesWrapper());
    }

    TmdbServiceWrapper(Tmdb tmdb, Context context, PreferencesWrapper preferencesWrapper) {
        this.searchService = tmdb.searchService();
        this.context = context;
        this.preferencesWrapper = preferencesWrapper;
    }

    public Call<MovieResultsPage> search(String name) {
        return searchService.movie(
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
