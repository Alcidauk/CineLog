package com.ulicae.cinelog.android.activities.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

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
public class AddTag extends AppCompatActivity {

    @BindView(R.id.tag_name)
    EditText tag_name;
    @BindView(R.id.tag_color)
    Button tag_color;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    TagDto tag;

    // private TagService tagDtoService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);


        tag = Parcels.unwrap(getIntent().getParcelableExtra("tag"));
        if (tag == null) {
            // setTitle(getString(R.string.title_activity_edit_tag_creation));
        } else {
            tag_name.setText(tag.getName());
            tag_color.setText(tag.getColor());
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        tag.setColor(tag_color.getText().toString());

        // TODO create or update : kino = (KinoDto) dtoService.createOrUpdate(kino);

        redirect();
    }

    private void redirect() {
        if (!getIntent().getBooleanExtra("creation", false)) {
            Intent returnIntent = getIntent();
            // TODO returnIntent.putExtra("kino", Parcels.wrap(kino));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Intent returnIntent = new Intent(this, ViewKino.class);
            // returnIntent.putExtra("kino", Parcels.wrap(kino));
            // TODO returnIntent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(returnIntent);
        }

        finish();
    }

}