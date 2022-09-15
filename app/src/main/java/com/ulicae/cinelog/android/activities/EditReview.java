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
import com.ulicae.cinelog.data.ServiceFactory;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.ActivityEditReviewBinding;
import com.ulicae.cinelog.databinding.ContentEditReviewBinding;
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

    private ContentEditReviewBinding binding;
    private ActivityEditReviewBinding activityBinding;

    KinoDto kino;

    private DataService dtoService;
    private TagService tagService;

    private WishlistItemDeleter wishlistItemDeleter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        wishlistItemDeleter = new WishlistItemDeleter(this);

        activityBinding = ActivityEditReviewBinding.inflate(getLayoutInflater());
        binding = activityBinding.contentEditReview;
        setContentView(activityBinding.getRoot());

        String dtoType = getIntent().getStringExtra("dtoType");
        dtoService = new ServiceFactory(getBaseContext()).create(dtoType, ((KinoApplication) getApplicationContext()).getDaoSession());

        tagService = new TagService(((KinoApplication) getApplication()).getDaoSession());

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        if (getIntent().getBooleanExtra("creation", false)) {
            setTitle(getString(R.string.title_activity_add_review_creation));
        }

        int maxRating;
        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }

        String[] displayedValues = getDisplayedValues(maxRating);

        binding.ratingPicker.setMinValue(0);
        binding.ratingPicker.setMaxValue(maxRating * 2);

        binding.ratingPicker.setDisplayedValues(displayedValues);

        binding.ratingPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String[] displayedValues = picker.getDisplayedValues();
                float rating = Float.parseFloat(displayedValues[newVal]);

                binding.reviewEditRatingBarAsText.setText(String.format("%s", rating));
                kino.setRating(rating);
            }
        });

        initRating(displayedValues);

        if (kino.getReview() != null) {
            binding.kinoReviewText.setText(kino.getReview());
        }

        if (kino.getReview_date() != null) {
            String review_date_as_string = null;
            if (kino.getReview_date() != null) {
                review_date_as_string = DateFormat.getDateFormat(getBaseContext()).format(kino.getReview_date());
            }
            binding.kinoReviewDateButton.setText(review_date_as_string);
        }

        binding.viewKinoTitleEdit.setText(kino.getTitle());
        binding.viewKinoTitleReadonly.setText(kino.getTitle());
        if (kino.getTmdbKinoId() != null) {
            binding.viewKinoTitleEdit.setVisibility(View.INVISIBLE);
            binding.viewKinoTitleReadonly.setVisibility(View.VISIBLE);
        } else {
            binding.viewKinoTitleEdit.setVisibility(View.VISIBLE);
            binding.viewKinoTitleReadonly.setVisibility(View.INVISIBLE);
        }

        binding.reviewTagEdit.setOnClickListener(onReviewTagEdit());
        activityBinding.fabSave.setOnClickListener(view -> onFabSaveClick());

        setSupportActionBar(activityBinding.contentToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @NonNull
    private View.OnClickListener onReviewTagEdit() {
        return view -> {
            TagChooserDialog dialog = new TagChooserDialog(tagService, kino);
            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
        };
    }

    private void initRating(String[] displayedValues) {
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        int maxRating;
        if (kino.getMaxRating() == null) {
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }

        if (kino.getRating() != null) {
            binding.ratingPicker.setValue(getValueToDisplay(displayedValues, kino.getRating()));
            binding.reviewEditRatingBarAsText.setText(String.format("%s", kino.getRating()));
        }

        binding.reviewEditRatingBarMaxAsText.setText(String.format("/%s", maxRating));
    }

    private int getValueToDisplay(String[] displayedValues, float rating) {
        int i = 0;
        for (String value : displayedValues) {
            if (Float.parseFloat(value) == rating) {
                return i;
            }

            i++;
        }

        return 0;
    }

    @NonNull
    private String[] getDisplayedValues(int maxRating) {
        List<String> displayedValues = new ArrayList<>();

        for (int i = 0; i <= maxRating; i++) {
            displayedValues.add(String.valueOf(i));

            if (i != maxRating) {
                displayedValues.add(i + ".5");
            }
        }

        return displayedValues.toArray(new String[0]);
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

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onFabSaveClick() {
        kino.setReview(binding.kinoReviewText.getText().toString());

        if (kino.getTmdbKinoId() == null) {
            kino.setTitle(binding.viewKinoTitleEdit.getText().toString());
        }

        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String maxRating = prefs.getString("default_max_rate_value", "5");
            int maxRatingAsInt = Integer.parseInt(maxRating);
            kino.setMaxRating(maxRatingAsInt);
        }

        //noinspection unchecked
        kino = (KinoDto) dtoService.createOrUpdate(kino);

        long wishlistId = getIntent().getLongExtra("wishlistId", 0L);
        if (wishlistId != 0L) {
            wishlistItemDeleter.deleteWishlistItem(wishlistId, getIntent().getStringExtra("dtoType"));
        }

        redirect();

    }

    private void redirect() {
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            KinoDto kino = ((EditReview) getActivity()).kino;
            kino.setReview_date(c.getTime());
            String review_date_as_string = null;
            if (kino.getReview_date() != null) {
                review_date_as_string = DateFormat.getDateFormat(getActivity().getBaseContext()).format(kino.getReview_date());
            }
            ((EditReview) getActivity()).binding.kinoReviewDateButton.setText(review_date_as_string);
            // Do something with the date chosen by the user
        }
    }

}