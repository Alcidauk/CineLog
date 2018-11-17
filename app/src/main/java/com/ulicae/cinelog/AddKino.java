package com.ulicae.cinelog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ulicae.cinelog.dto.KinoDto;
import com.ulicae.cinelog.dto.KinoService;
import com.ulicae.cinelog.network.task.MovieNetworkTaskCreator;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.ulicae.cinelog.network.TmdbServiceWrapper;
import com.ulicae.cinelog.network.task.TvNetworkTaskCreator;
import com.uwetrottmann.tmdb2.entities.Movie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
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
public class AddKino extends AppCompatActivity {

    static final int RESULT_VIEW_KINO = 4;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.kino_search)
    EditText kino_search;
    @BindView(R.id.kino_results)
    ListView kino_results_list;
    @BindView(R.id.kino_search_progress_bar)
    ProgressBar kino_search_progress_bar;

    private TmdbServiceWrapper tmdbServiceWrapper;
    private NetworkTaskManager networkTaskManager;

    private KinoService kinoService;

    private Handler handler;

    private final static int TRIGGER_SERACH = 1;

    // Where did 1000 come from? It's arbitrary, since I can't find average android typing speed.
    private final static long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    static int RESULT_EDIT_REVIEW = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kino);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tmdbServiceWrapper = new TmdbServiceWrapper(this);
        networkTaskManager = new NetworkTaskManager(this, new MovieNetworkTaskCreator());

        kinoService = new KinoService(((KinoApplication) getApplication()).getDaoSession());

        handler = new AddKinoHandler(new WeakReference<>(this));
    }

    private void startSearchTask() {
        if (isNetworkAvailable()) {
            new NetworkTaskManager(this, new TvNetworkTaskCreator()).createAndExecute(
                    tmdbServiceWrapper.searchTv(kino_search.getText().toString())
            );

            Call<MovieResultsPage> search = tmdbServiceWrapper.search(kino_search.getText().toString());
            networkTaskManager.createAndExecute(search);
        } else {
            Toast t = Toast.makeText(getApplicationContext(),
                    getString(R.string.addkino_error_no_network),
                    Toast.LENGTH_LONG);
            t.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused")
    @OnTextChanged(R.id.kino_search)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            kino_search_progress_bar.setVisibility(View.VISIBLE);
            handler.removeMessages(TRIGGER_SERACH);
            handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
        } else if (count == 0) {
            clearListView();
        }
    }

    public void clearListView() {
        if (kino_results_list != null && kino_results_list.getAdapter() != null) {
            kino_results_list.setAdapter(null);
        }
        kino_search_progress_bar.setVisibility(View.GONE);
    }

    @OnClick(R.id.kino_search_add_from_scratch)
    public void onClick(View view) {
        KinoDto kinoToCreate = new KinoDto();
        kinoToCreate.setTitle(kino_search.getText().toString());

        // TODO factorize
        Intent intent = new Intent(view.getContext(), EditReview.class);

        intent.putExtra("kino", Parcels.wrap(kinoToCreate));

        startActivity(intent);
    }

    public void populateListView(final List<Movie> movies) {
        if (kino_results_list != null) {
            kino_results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> view, View parent, final int position, long rowId) {
                    Movie movie = movies.get(position);

                    KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(movie.id);
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
            });

            kino_results_list.setAdapter(new KinoResultsAdapter(this, movies));
            kino_search_progress_bar.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        @BindView(R.id.kino_title)
        TextView title;
        @BindView(R.id.kino_year)
        TextView year;
        @BindView(R.id.kino_poster)
        ImageView poster;

        @BindView(R.id.add_review_button)
        ImageButton add_review_button;

        @BindView(R.id.kino_rating_bar_review)
        RatingBar kino_rating_bar_review;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class AddKinoHandler extends Handler {
        private WeakReference<AddKino> addKino;

        AddKinoHandler(WeakReference<AddKino> addKino) {
            this.addKino = addKino;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                addKino.get().startSearchTask();
            }
        }
    }
}


