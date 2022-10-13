package com.ulicae.cinelog.android.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.ViewSerieFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.V2ViewSerieHostBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

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
public class ViewSerie extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2ViewSerieHostBinding binding = V2ViewSerieHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            ViewSerieFragment fragment = new ViewSerieFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.view_serie_host, fragment)
                    .commit();
        }
    }

    public void goToSerieEdition(KinoDto kino) {
        Intent intent = new Intent(getApplicationContext(), EditReview.class);
        intent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
        intent.putExtra("kino", Parcels.wrap(kino));
        startActivity(intent);
    }
}
