package com.ulicae.cinelog.network.task.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.TvShow;

import java.io.IOException;

import retrofit2.Call;

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
public class SerieSyncNetworkTask extends AsyncTask<Call<TvShow>, Void, SerieDto> {

    private final SerieBuilderFromMovie serieBuilderFromMovie;
    private SerieReview serieReview;
    private SerieService serieService;

    public SerieSyncNetworkTask(SerieBuilderFromMovie serieBuilderFromMovie, SerieReview serieReview, SerieService serieService) {
        this.serieService = serieService;
        this.serieBuilderFromMovie = serieBuilderFromMovie;
        this.serieReview = serieReview;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SerieDto doInBackground(Call<TvShow>... calls) {
        for (Call<TvShow> call : calls) {
            try {
                TvShow tvShow = call.execute().body();
                return serieBuilderFromMovie.build(tvShow);
            } catch (IOException e) {
                Log.i("tmdbGetter", "Unable to execute task.");
            }
        }
        return null;
    }

    protected void onPostExecute(SerieDto updatedDto) {
        serieService.updateTmdbInfo(updatedDto, serieReview);
    }
}
