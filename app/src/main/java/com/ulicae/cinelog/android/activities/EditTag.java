package com.ulicae.cinelog.android.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.EditTagFragment;
import com.ulicae.cinelog.databinding.V2EditTagHostBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

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


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2EditTagHostBinding binding = V2EditTagHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            EditTagFragment fragment = new EditTagFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.edit_tag_host, fragment)
                    .commit();
        }
    }


}