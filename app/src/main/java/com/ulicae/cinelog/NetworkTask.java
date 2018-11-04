package com.ulicae.cinelog;

import android.os.AsyncTask;

import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;

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
