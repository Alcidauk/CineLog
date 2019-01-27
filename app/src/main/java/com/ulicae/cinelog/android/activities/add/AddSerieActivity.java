package com.ulicae.cinelog.android.activities.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.android.activities.add.wishlist.WishlistTvResultsAdapter;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.ulicae.cinelog.network.task.TvNetworkTaskCreator;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
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
public class AddSerieActivity extends AddReviewActivity<BaseTvShow> {

    static final int RESULT_VIEW_KINO = 4;

    @BindView(R.id.kino_search)
    EditText kinoSearchEditText;

    @BindView(R.id.kino_search_add_from_scratch)
    Button addFromScratchButton;

    private boolean toWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO constant with toWishlist
        toWishlist = getIntent().getBooleanExtra("toWishlist", false);

        networkTaskManager = new NetworkTaskManager(this, new TvNetworkTaskCreator());
        dataService = new SerieService(((KinoApplication) getApplication()).getDaoSession(), getBaseContext());

        addFromScratchButton.setText(getString(R.string.add_serie_from_scratch_label));
        kinoSearchEditText.setHint(getString(R.string.serie_title_hint));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_serie;
    }

    @Override
    protected void executeTask(String textToSearch) {
        Call<TvShowResultsPage> search = tmdbServiceWrapper.searchTv(kino_search.getText().toString());
        networkTaskManager.createAndExecute(search);
    }

    @OnClick(R.id.kino_search_add_from_scratch)
    public void onClick(View view) {
        Intent intent;
        if (!toWishlist) {
            SerieDto serieDto = new SerieDto();
            serieDto.setTitle(kino_search.getText().toString());

            intent = new Intent(view.getContext(), EditReview.class);
            intent.putExtra("kino", Parcels.wrap(serieDto));
            intent.putExtra("dtoType", "serie");
        } else {
            intent = new Intent(view.getContext(), ViewDataActivity.class);
            intent.putExtra("dataDto", Parcels.wrap(
                    new WishlistDataDto(kino_search.getText().toString(), WishlistItemType.SERIE))
            );
        }

        startActivity(intent);
    }

    public void populateListView(final List<BaseTvShow> tvShows) {
        ArrayAdapter<BaseTvShow> arrayAdapter;
        if(!toWishlist){
            arrayAdapter = new TvResultsAdapter(this, tvShows);
        } else {
            arrayAdapter = new WishlistTvResultsAdapter(this, tvShows);
        }

        if (kino_results_list != null) {
            kino_results_list.setAdapter(arrayAdapter);
            kino_search_progress_bar.setVisibility(View.GONE);
        }
    }

}


