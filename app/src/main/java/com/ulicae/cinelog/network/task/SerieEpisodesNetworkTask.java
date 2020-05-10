package com.ulicae.cinelog.network.task;

import android.os.AsyncTask;

import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.network.TmdbServiceWrapper;
import com.uwetrottmann.tmdb2.entities.TvEpisode;

import java.util.List;

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
public class SerieEpisodesNetworkTask extends AsyncTask<Integer, Void, List<TvEpisode>> {

    private ViewKino context;

    private List<TvEpisode> episodes;

    public SerieEpisodesNetworkTask(ViewKino context) {
        this.context = context;
    }


    @Override
    protected List<TvEpisode> doInBackground(Integer... integers) {
        for (Integer integer : integers) {
            TmdbServiceWrapper tmdbServiceWrapper = new TmdbServiceWrapper(context);
            List<Integer> tvSeasonNumbers = tmdbServiceWrapper.tvShowSeasons(integer);

            episodes = tmdbServiceWrapper.tvShowEpisodes(integer, tvSeasonNumbers);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<TvEpisode> tvEpisodes) {
        // context.populateEpisodeList(episodes);
    }
}
