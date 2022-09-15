package com.ulicae.cinelog.android.activities;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.ActivityAddTagBinding;
import com.ulicae.cinelog.databinding.ContentAddTagBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import java.util.Objects;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
public class EditTag extends AppCompatActivity {

    private ContentAddTagBinding binding;

    TagDto tag;

    private TagService tagDtoService;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        ActivityAddTagBinding activityBinding = ActivityAddTagBinding.inflate(getLayoutInflater());
        binding = activityBinding.addTagContent;
        setContentView(activityBinding.getRoot());

        tagDtoService = new TagService(((KinoApplication) getApplication()).getDaoSession());

        tag = Parcels.unwrap(getIntent().getParcelableExtra("tag"));
        if (tag == null) {
            tag = new TagDto();
            tag.setColor(getString(R.color.colorPrimary));
        } else {
            binding.tagName.setText(tag.getName());
            binding.tagFilms.setChecked(tag.isForMovies());
            binding.tagSeries.setChecked(tag.isForSeries());
        }

        activityBinding.fabSaveTag.setOnClickListener(view -> EditTag.this.onClick());
        binding.tagFilms.setOnCheckedChangeListener((compoundButton, b) -> onFilmsCheckedChanged(b));
        binding.tagSeries.setOnCheckedChangeListener((compoundButton, b) -> onSeriesCheckedChanged(b));
        binding.tagColorUpdate.setOnClickListener(this::onTagColorUpdate);

        fetchColor();

        setSupportActionBar(activityBinding.addTagToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    public void onClick() {
        tag.setName(binding.tagName.getText().toString());

        if (tag.getName() == null || tag.getName().isEmpty()) {
            Toast.makeText(
                            getBaseContext(),
                            getBaseContext().getString(R.string.edit_tag_name_not_filled),
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        tagDtoService.createOrUpdate(tag);

        finish();
    }

    public void onFilmsCheckedChanged(boolean checked) {
        tag.setForMovies(checked);
    }

    public void onSeriesCheckedChanged(boolean checked) {
        tag.setForSeries(checked);
    }

    private void fetchColor() {
        if (tag.getColor() != null) {
            binding.tagColorCurrent.setBackgroundColor(Color.parseColor(tag.getColor()));
        }
    }

    public void onTagColorUpdate(View view) {
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.save),
                        (ColorEnvelopeListener) (envelope, fromUser) -> {
                            tag.setColor("#" + envelope.getHexCode().substring(2));
                            fetchColor();
                        })
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false)
                .setBottomSpace(12)
                .show();
    }

}