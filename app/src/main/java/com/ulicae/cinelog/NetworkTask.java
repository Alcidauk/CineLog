package com.ulicae.cinelog;

import android.os.AsyncTask;

import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.lang.ref.WeakReference;
import java.util.List;

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
public class NetworkTask extends AsyncTask<Call<MovieResultsPage>, Void, List<Movie>> {

    private WeakReference<AddKino> addKino;

    public NetworkTask(AddKino addKino) {
        this.addKino = new WeakReference<>(addKino);
    }

    protected List<Movie> doInBackground(Call<MovieResultsPage>... results) {
        List<Movie> movies = null;
        try {
            if (!isCancelled()) {
                movies = results[0].execute().body().results;
            }
        } catch (java.io.IOException e) {
            return null;
        }
        return movies;
    }

    protected void onPostExecute(List<Movie> movies) {
        if (!isCancelled()) {
            populateListView(movies);
        }
    }

    private void populateListView(final List<Movie> movies) {
        addKino.get().populateListView(movies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkTask that = (NetworkTask) o;

        return addKino != null && addKino.get() != null ? addKino.get().equals(that.addKino.get()) : that.addKino == null;
    }

    @Override
    public int hashCode() {
        return addKino.get() != null ? addKino.get().hashCode() : 0;
    }
}
