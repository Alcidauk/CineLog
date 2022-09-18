package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.ActivityViewUnregisteredKinoBinding;
import com.ulicae.cinelog.databinding.ContentKinoViewUnregisteredBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

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
public class ViewUnregisteredKino extends AppCompatActivity {

    KinoDto kino;
    int position;
    boolean editted = false;

    private static final int RESULT_ADD_REVIEW = 3;
    private ActivityViewUnregisteredKinoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);


        binding = ActivityViewUnregisteredKinoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditReview.class);
            intent.putExtra("kino", Parcels.wrap(kino));
            intent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));

            startActivityForResult(intent, RESULT_ADD_REVIEW);
        });

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position", -1);

        configureLabels(getIntent().getStringExtra("dtoType"));

        setSupportActionBar(binding.viewUnregisteredToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureLabels(String dtoType) {
        if (dtoType.equals("serie")) {
            setTitle(R.string.title_activity_view_unregistered_serie);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ContentKinoViewUnregisteredBinding viewUnregisteredContent = binding.viewUnregisteredContent;

        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + kino.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(viewUnregisteredContent.viewKinoTmdbImageLayout);
        }
        viewUnregisteredContent.viewKinoTmdbYear.setText(kino.getReleaseDate());
        viewUnregisteredContent.viewKinoTmdbOverview.setText(kino.getOverview());

        viewUnregisteredContent.viewKinoTmdbTitle.setText(kino.getTitle());
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
        if (item.getItemId() == android.R.id.home) {
            if (editted) {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
                returnIntent.putExtra("kino", Parcels.wrap(kino));
                returnIntent.putExtra("kino_position", position);
                setResult(Activity.RESULT_OK, returnIntent);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
