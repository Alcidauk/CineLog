package com.ulicae.cinelog.android.activities.add;

import android.os.Bundle;
import android.view.View;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.KinoService;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.ulicae.cinelog.network.task.TvNetworkTaskCreator;
import com.uwetrottmann.tmdb2.entities.TvResultsPage;
import com.uwetrottmann.tmdb2.entities.TvShow;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;

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
public class AddSerieActivity extends AddReviewActivity<TvShow> {

    static final int RESULT_VIEW_KINO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkTaskManager = new NetworkTaskManager(this, new TvNetworkTaskCreator());
        dataService = new KinoService(((KinoApplication) getApplication()).getDaoSession());
    }

    @Override
    protected void executeTask(String textToSearch) {
        Call<TvResultsPage> search = tmdbServiceWrapper.searchTv(kino_search.getText().toString());
        networkTaskManager.createAndExecute(search);
    }

    @OnClick(R.id.kino_search_add_from_scratch)
    public void onClick(View view) {
        /*KinoDto kinoToCreate = new KinoDto();
        kinoToCreate.setTitle(kino_search.getText().toString());

        // TODO factorize
        Intent intent = new Intent(view.getContext(), EditReview.class);

        intent.putExtra("kino", Parcels.wrap(kinoToCreate));

        startActivity(intent);*/
    }

    public void populateListView(final List<TvShow> tvShows) {
        if (kino_results_list != null) {
            /*kino_results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Movie movie = movies.get(position);

                    KinoDto kinoByTmdbMovieId = ((KinoService) dataService).getKinoByTmdbMovieId(movie.id);
                    if (kinoByTmdbMovieId == null) {
                        Intent intent = new Intent(view.getContext(), ViewUnregisteredKino.class);
                        KinoDto kino = new KinoBuilderFromMovie().build(movie);
                        intent.putExtra("kino", Parcels.wrap(kino));

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(view.getContext(), ViewKino.class);
                        intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                        intent.putExtra("kino_position", position);
                        startActivityForResult(intent, RESULT_VIEW_KINO);
                    }
                }
            });*/

            kino_results_list.setAdapter(new TvResultsAdapter(this, tvShows));
            kino_search_progress_bar.setVisibility(View.GONE);
        }
    }

}


