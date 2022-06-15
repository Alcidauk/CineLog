package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class ViewUnregisteredKino extends AppCompatActivity {
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

    KinoDto kino;
    int position;
    boolean editted = false;

    private static final int RESULT_ADD_REVIEW = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_view_unregistered_kino);
        ButterKnife.bind(this);

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position", -1);

        configureLabels(getIntent().getStringExtra("dtoType"));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureLabels(String dtoType) {
        if(dtoType.equals("serie")){
            setTitle(R.string.title_activity_view_unregistered_serie);
        }
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        Intent intent = new Intent(this, EditReview.class);
        intent.putExtra("kino", Parcels.wrap(kino));
        intent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));

        startActivityForResult(intent, RESULT_ADD_REVIEW);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + kino.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }
        year.setText(kino.getReleaseDate());
        overview.setText(kino.getOverview());

        title.setText(kino.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
