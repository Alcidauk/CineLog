package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class ViewKino extends AppCompatActivity {

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
    @BindView(R.id.view_kino_review_rating)
    RatingBar rating;
    @BindView(R.id.view_kino_review_rating_as_text)
    TextView ratingAsText;
    @BindView(R.id.view_kino_review_review)
    TextView review;
    @BindView(R.id.view_kino_review_review_date)
    TextView review_date;

    KinoDto kino;
    int position;
    boolean editted = false;

    private static final int RESULT_ADD_REVIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kino);
        ButterKnife.bind(this);

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position", -1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        int maxRating;
        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }
        rating.setNumStars(maxRating);
    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText(kino.getTitle());
        if (kino.getPosterPath() != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + kino.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }
        year.setText(kino.getReleaseDate());
        overview.setText(kino.getOverview());

        if (kino.getRating() != null) {
            rating.setRating(kino.getRating());
        }
        rating.setStepSize(0.5f);

        ratingAsText.setText(String.format("%s", kino.getRating()));

        review.setText(kino.getReview());
        review_date.setText(getReviewDateAsString(kino.getReview_date()));
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Intent intent = new Intent(this, EditReview.class);
        intent.putExtra("kino", Parcels.wrap(kino));
        startActivityForResult(intent, RESULT_ADD_REVIEW);
    }

    private String getReviewDateAsString(Date review_date) {
        if (review_date != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(review_date);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                kino = Parcels.unwrap(data.getParcelableExtra("kino"));
                editted = true;
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (editted) {
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("kino", Parcels.wrap(kino));
                    returnIntent.putExtra("kino_position", position);
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
