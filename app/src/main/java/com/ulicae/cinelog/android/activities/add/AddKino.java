package com.ulicae.cinelog.android.activities.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.android.activities.add.wishlist.WishlistMovieResultsAdapter;
import com.ulicae.cinelog.android.activities.view.ViewDataActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.databinding.ActivityAddKinoBinding;
import com.ulicae.cinelog.databinding.ContentAddReviewBinding;
import com.ulicae.cinelog.databinding.ToolbarBinding;
import com.ulicae.cinelog.network.task.MovieNetworkTaskCreator;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class AddKino extends AddReviewActivity<BaseMovie> {

    static final int RESULT_VIEW_KINO = 4;

    private boolean toWishlist;

    private ActivityAddKinoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toWishlist = getIntent().getBooleanExtra("toWishlist", false);

        networkTaskManager = new NetworkTaskManager(this, new MovieNetworkTaskCreator());
        dataService = new KinoService(((KinoApplication) getApplication()).getDaoSession());
    }

    @Override
    protected void inflateBinding() {
        binding = ActivityAddKinoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected ToolbarBinding getToolbar() {
        return binding.addKinoToolbar;
    }

    @Override
    protected ContentAddReviewBinding getContentAddReviewBinding() {
        return binding.addKinoContent;
    }

    @Override
    protected void executeTask(String textToSearch) {
        Call<MovieResultsPage> search = tmdbServiceWrapper.search(getContentAddReviewBinding().kinoSearch.getText().toString());
        networkTaskManager.createAndExecute(search);
    }

    @Override
    public void onFromScratchClick(View view) {
        Intent intent;
        if (!toWishlist) {
            KinoDto kinoToCreate = new KinoDto();
            kinoToCreate.setTitle(getContentAddReviewBinding().kinoSearch.getText().toString());

            intent = new Intent(view.getContext(), EditReview.class);
            intent.putExtra("kino", Parcels.wrap(kinoToCreate));
            intent.putExtra("dtoType", "kino");
        } else {
            intent = new Intent(view.getContext(), ViewDataActivity.class);
            intent.putExtra("dataDto", Parcels.wrap(
                    new WishlistDataDto(getContentAddReviewBinding().kinoSearch.getText().toString(), WishlistItemType.MOVIE))
            );
        }

        startActivity(intent);
    }

    public void populateListView(final List<BaseMovie> movies) {
        ArrayAdapter<BaseMovie> arrayAdapter;
        if (!toWishlist) {
            arrayAdapter = new KinoResultsAdapter(this, movies);
        } else {
            arrayAdapter = new WishlistMovieResultsAdapter(this, movies);
        }

        getContentAddReviewBinding().kinoResults.setAdapter(arrayAdapter);
        getContentAddReviewBinding().kinoSearchProgressBar.setVisibility(View.GONE);
    }

}


