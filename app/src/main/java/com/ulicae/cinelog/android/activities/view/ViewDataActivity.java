package com.ulicae.cinelog.android.activities.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.SerieDataService;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
public class ViewDataActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.view_kino_tmdb_image_layout)
    ImageView poster;
    @BindView(R.id.view_kino_tmdb_title)
    TextView title;
    @BindView(R.id.view_kino_tmdb_year)
    TextView year;
    @BindView(R.id.view_kino_tmdb_overview)
    TextView overview;

    private WishlistDataDto wishlistDataDto;
    private boolean isWishlist;

    private static final int RESULT_ADD_REVIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_view_unregistered_kino);
        ButterKnife.bind(this);

        wishlistDataDto = Parcels.unwrap(getIntent().getParcelableExtra("dataDto"));
        isWishlist = getIntent().getBooleanExtra("isWishlist", false);

        if(wishlistDataDto.getId() != null){
            fab.setVisibility(View.INVISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        if(isWishlist){
            if(wishlistDataDto.getId() == null){
                SerieDataService serieDataService = new SerieDataService(((KinoApplication) getApplicationContext()).getDaoSession());
                serieDataService.createSerieData(wishlistDataDto);

                Toast.makeText(getApplicationContext(), getString(R.string.wishlist_item_added), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (wishlistDataDto.getPosterPath() != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + wishlistDataDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }

        year.setText(wishlistDataDto.getReleaseDate());
        overview.setText(wishlistDataDto.getOverview());
        title.setText(wishlistDataDto.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                wishlistDataDto = Parcels.unwrap(data.getParcelableExtra("kino"));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
