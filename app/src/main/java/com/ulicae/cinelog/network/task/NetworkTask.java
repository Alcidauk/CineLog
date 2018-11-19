package com.ulicae.cinelog.network.task;

import android.os.AsyncTask;

import com.ulicae.cinelog.android.activities.add.AddReviewActivity;
import com.uwetrottmann.tmdb2.entities.BaseResultsPage;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

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
public abstract class NetworkTask<T extends BaseResultsPage, D> extends AsyncTask<Call<T>, Void, List<D>> {

    private WeakReference<AddReviewActivity> addReviewActivityWeakReference;

    NetworkTask(AddReviewActivity addReviewActivityWeakReference) {
        this.addReviewActivityWeakReference = new WeakReference<>(addReviewActivityWeakReference);
    }

    protected List<D> doInBackground(Call<T>... results) {
        List<D> movies = null;
        try {
            if (!isCancelled()) {
                Response<T> response = results[0].execute();

                if (response != null && response.body() != null) {
                    movies = getResults(response);
                } else {
                    cancel();
                }
            }
        } catch (java.io.IOException e) {
            // TODO deal with an error toast
            cancel();
        }
        return movies;
    }

    abstract List<D> getResults(Response<T> response);

    protected void onPostExecute(List<D> movies) {
        if (!isCancelled()) {
            populateListView(movies);
        }
    }

    abstract void populateListView(final List<D> movies);

    private void cancel() {
        cancel(true);
        addReviewActivityWeakReference.get().clearListView();
    }

    public WeakReference<AddReviewActivity> getAddReviewActivityWeakReference() {
        return addReviewActivityWeakReference;
    }
}
