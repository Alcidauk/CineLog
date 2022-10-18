package com.ulicae.cinelog.android.activities.add;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.EditReviewFragment;
import com.ulicae.cinelog.databinding.V2AddKinoHostBinding;
import com.ulicae.cinelog.databinding.V2EditReviewHostBinding;

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
public class AddKino extends AppCompatActivity {

    static final int RESULT_VIEW_KINO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        V2AddKinoHostBinding binding = V2AddKinoHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            AddKinoFragment fragment = new AddKinoFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_kino_host, fragment)
                    .commit();
        }
    }

}


