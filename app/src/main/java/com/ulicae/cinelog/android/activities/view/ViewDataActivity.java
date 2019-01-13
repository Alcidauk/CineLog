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

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.data.SerieDataService;
import com.ulicae.cinelog.data.ServiceFactory;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.data.SerieDataDto;
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

    private SerieDataDto serieDataDto;
    private boolean isWishlist;

    private static final int RESULT_ADD_REVIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_view_unregistered_kino);
        ButterKnife.bind(this);

        serieDataDto = Parcels.unwrap(getIntent().getParcelableExtra("dataDto"));
        isWishlist = getIntent().getBooleanExtra("isWishlist", false);

        if(serieDataDto.getId() != null){
            fab.setVisibility(View.INVISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        if(isWishlist){
            if(serieDataDto.getId() == null){
                SerieDataService serieDataService = new SerieDataService(((KinoApplication) getApplicationContext()).getDaoSession());
                serieDataService.createSerieData(serieDataDto);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (serieDataDto.getPosterPath() != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + serieDataDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }

        year.setText(serieDataDto.getReleaseDate());
        overview.setText(serieDataDto.getOverview());
        title.setText(serieDataDto.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                serieDataDto = Parcels.unwrap(data.getParcelableExtra("kino"));
            }
        }
    }

}
