package com.ulicae.cinelog.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.add.AddKino;
import com.ulicae.cinelog.android.activities.add.AddSerieActivity;
import com.ulicae.cinelog.android.activities.fragments.ElementListFragment;
import com.ulicae.cinelog.android.activities.fragments.reviews.MovieFragment;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.databinding.V2ElementListHostBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.ulicae.cinelog.utils.UpgradeFixRunner;

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
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        V2ElementListHostBinding binding = V2ElementListHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            ElementListFragment fragment = new ElementListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.element_list_host, fragment)
                    .commit();
        }

        checkNeededFix();
    }

    private void checkNeededFix() {
        new UpgradeFixRunner(getBaseContext(), getApplication()).runFixesIfNeeded();
    }

    public void addElement(Fragment fragment) {
        Intent intent;
        if (fragment instanceof MovieFragment) {
            intent = new Intent(getApplicationContext(), AddKino.class);
        } else {
            intent = new Intent(getApplicationContext(), AddSerieActivity.class);
        }

        intent.putExtra("toWishlist", false);

        startActivity(intent);
    }

    public void goToWishlist() {
        launchActivity(WishlistActivity.class);
    }

    public void goToTags() {
        launchActivity(TagsActivity.class);
    }

    public void goToImport() {
        launchActivity(ImportInDb.class);
    }

    public void goToExport() {
        launchActivity(ExportDb.class);
    }

    public void goToSettings() {
        launchActivity(SettingsActivity.class);
    }

    private void launchActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(getApplicationContext(), activity));
    }
}