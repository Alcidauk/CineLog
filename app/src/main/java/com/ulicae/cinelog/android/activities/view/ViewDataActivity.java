package com.ulicae.cinelog.android.activities.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.fragments.review.edit.EditReviewFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.databinding.V2WishlistItemHostBinding;
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
public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2WishlistItemHostBinding binding = V2WishlistItemHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            WishlistItemFragment fragment = new WishlistItemFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nav_host_fragment, fragment)
                    .commit();
        }
    }

    public void createReview(Long wishlistId, KinoDto dto) {
        // TODO verify
        navigateToReviewCreation(wishlistId, dto);
    }

    public void navigateToReviewCreation(Long wishlistId, KinoDto kinoDto) {
        Fragment fragment = new EditReviewFragment();

        Bundle args = new Bundle();
        args.putString("dtoType", kinoDto instanceof SerieDto ? "serie" : "kino");
        args.putParcelable("kino", Parcels.wrap(kinoDto));
        args.putBoolean("creation", true);
        args.putLong("wishlistId", wishlistId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("EditReview")
                .replace(R.id.nav_host_fragment, fragment, "EditReview")
                .commit();
    }

}
