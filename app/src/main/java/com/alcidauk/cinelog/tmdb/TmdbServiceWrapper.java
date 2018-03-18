package com.alcidauk.cinelog.tmdb;


import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;

import retrofit2.Call;

public class TmdbServiceWrapper {


    private SearchService searchService;

    private static final String API_KEY = "d6d6579b3a02efda2efde4585120d45e";

    public TmdbServiceWrapper() {
        this.searchService = new Tmdb(API_KEY).searchService();
    }

    TmdbServiceWrapper(Tmdb tmdb) {
        this.searchService = tmdb.searchService();
    }

    public Call<MovieResultsPage> search(String name) {
        return searchService.movie(name, 1, null, null, null, null, "ngram");
    }
}
