package com.ulicae.cinelog.android.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

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

    @BindView(R.id.tag_name)
    EditText tag_name;

    @BindView(R.id.tag_films)
    CheckBox tag_films;

    @BindView(R.id.tag_series)
    CheckBox tag_series;

    @BindView(R.id.tag_color_current)
    View tag_color;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    TagDto tag;

    private TagService tagDtoService;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);

        tagDtoService = new TagService(((KinoApplication) getApplication()).getDaoSession());

        tag = Parcels.unwrap(getIntent().getParcelableExtra("tag"));
        if (tag == null) {
            tag = new TagDto();
            tag.setColor(getString(R.color.colorPrimary));
        } else {
            tag_name.setText(tag.getName());
            tag_films.setChecked(tag.isForMovies());
            tag_series.setChecked(tag.isForSeries());
        }

        fetchColor();

        setSupportActionBar(toolbar);
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

    @OnClick(R.id.fab_save_tag)
    public void onClick() {
        tag.setName(tag_name.getText().toString());

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

    @OnCheckedChanged(R.id.tag_films)
    public void onFilmsCheckedChanged(boolean checked) {
        tag.setForMovies(checked);
    }

    @OnCheckedChanged(R.id.tag_series)
    public void onSeriesCheckedChanged(boolean checked) {
        tag.setForSeries(checked);
    }

    private void fetchColor() {
        if (tag.getColor() != null) {
            tag_color.setBackgroundColor(Color.parseColor(tag.getColor()));
        }
    }

    @OnClick(R.id.tag_color_update)
    public void onClick(View view) {
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.save),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                tag.setColor("#" + envelope.getHexCode().substring(2));
                                fetchColor();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false)
                .setBottomSpace(12)
                .show();
    }

}