package com.ulicae.cinelog.android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.EditReviewFragment;
import com.ulicae.cinelog.android.v2.ViewUnregisteredItemFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.V2ViewUnregisteredItemHostBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2ViewUnregisteredItemHostBinding binding = V2ViewUnregisteredItemHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            ViewUnregisteredItemFragment fragment = new ViewUnregisteredItemFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.view_unregistered_item_host, fragment)
                    .commit();
        }
    }


    public void goToEditReview(KinoDto kino){
        // TODO move in mainactivity
        Fragment fragment = new EditReviewFragment();

        Bundle args = new Bundle();
        args.putString("dtoType", getIntent().getStringExtra("dtoType"));
        args.putParcelable("kino", Parcels.wrap(kino));
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("EditReview")
                .replace(R.id.nav_host_fragment, fragment, "EditReview")
                .commit();
    }

}
