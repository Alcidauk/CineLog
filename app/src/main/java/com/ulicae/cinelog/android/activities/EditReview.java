package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.fragments.TagChooserDialog;
import com.ulicae.cinelog.android.v2.EditReviewFragment;
import com.ulicae.cinelog.android.v2.TagListFragment;
import com.ulicae.cinelog.data.ServiceFactory;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.ActivityEditReviewBinding;
import com.ulicae.cinelog.databinding.ContentEditReviewBinding;
import com.ulicae.cinelog.databinding.V2EditReviewHostBinding;
import com.ulicae.cinelog.databinding.V2TagListHostBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
public class EditReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2EditReviewHostBinding binding = V2EditReviewHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            EditReviewFragment fragment = new EditReviewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.edit_review_host, fragment)
                    .commit();
        }
    }

    public void redirect(KinoDto kino) {
        if (!getIntent().getBooleanExtra("creation", false)) {
            Intent returnIntent = getIntent();
            returnIntent.putExtra("kino", Parcels.wrap(kino));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Intent returnIntent = new Intent(this, ViewKino.class);
            returnIntent.putExtra("kino", Parcels.wrap(kino));
            returnIntent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
        }

        finish();
    }



}