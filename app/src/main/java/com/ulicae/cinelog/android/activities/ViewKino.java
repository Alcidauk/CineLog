package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    @BindView(R.id.view_kino_review_review_label)
    TextView reviewLabel;
    @BindView(R.id.view_kino_review_review_date)
    TextView review_date;

    @BindView(R.id.view_kino_tmdb_overview_more_button)
    Button overview_more_button;

    KinoDto kino;
    int position;
    boolean editted = false;

    private static final int RESULT_ADD_REVIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_view_kino);
        ButterKnife.bind(this);

        configureLabels(getIntent().getStringExtra("dtoType"));

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position", -1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void configureLabels(String dtoType) {
        if(dtoType.equals("serie")){
            setTitle(R.string.title_activity_view_serie);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        int maxRating;
        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }
        rating.setNumStars(maxRating);
        rating.setStepSize(0.5f);

        title.setText(kino.getTitle());
        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(this)
                    .load(new ImageCacheDownloader(kino.getPosterPath())
                            .getPosterFinder().getImage(kino.getPosterPath()))
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }
        String releaseDateLocal = kino.getReleaseDate();
        if(releaseDateLocal != null && !"".equals(releaseDateLocal)) {
            SimpleDateFormat frenchSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            try {
                Date parsedDate = frenchSdf.parse(releaseDateLocal);
                String formattedDate = DateFormat.getDateFormat(getBaseContext()).format(parsedDate);
                year.setText(formattedDate);
            } catch (ParseException ignored) {
                year.setText(String.valueOf(kino.getYear()));
            }
        }
        overview.setText(kino.getOverview());
        if(kino.getOverview() == null || "".equals(kino.getOverview())){
            overview_more_button.setVisibility(View.INVISIBLE);
        }

        if (kino.getRating() != null) {
            rating.setRating(kino.getRating());
        }

        ratingAsText.setText(String.format("%s", kino.getRating()));

        if(kino.getReview() == null || "".equals(kino.getReview())) {
            review.setVisibility(View.INVISIBLE);
            reviewLabel.setVisibility(View.INVISIBLE);
        } else {
            review.setVisibility(View.VISIBLE);
            reviewLabel.setVisibility(View.VISIBLE);

            review.setText(kino.getReview());
        }
        review_date.setText(getReviewDateAsString(kino.getReview_date()));
    }

    @OnClick(R.id.view_kino_tmdb_overview_more_button)
    public void onToggleOverview(View view) {
        if(poster.getVisibility() == View.VISIBLE){
            poster.setVisibility(View.GONE);
            overview_more_button.setText(R.string.view_kino_overview_see_less);

            overview.setEllipsize(null);
            overview.setMaxLines(Integer.MAX_VALUE);
            title.setEllipsize(null);
            title.setMaxLines(Integer.MAX_VALUE);

            LinearLayout layout = (LinearLayout) findViewById(R.id.view_kino_tmdb_image_title_layout);
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layout.setLayoutParams(params);
        } else {
            poster.setVisibility(View.VISIBLE);
            overview_more_button.setText(R.string.view_kino_overview_see_more);

            overview.setEllipsize(TextUtils.TruncateAt.END);
            overview.setMaxLines(4);
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setMaxLines(2);

            LinearLayout layout = (LinearLayout) findViewById(R.id.view_kino_tmdb_image_title_layout);
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    200, getResources().getDisplayMetrics());
            layout.setLayoutParams(params);
        }
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Intent intent = new Intent(this, EditReview.class);
        intent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
        intent.putExtra("kino", Parcels.wrap(kino));
        startActivityForResult(intent, RESULT_ADD_REVIEW);
    }

    private String getReviewDateAsString(Date review_date) {
        if (review_date != null) {
            return DateFormat.getDateFormat(getBaseContext()).format(review_date);
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
                    returnIntent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
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
