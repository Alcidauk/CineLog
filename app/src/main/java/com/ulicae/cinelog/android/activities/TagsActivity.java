package com.ulicae.cinelog.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.databinding.ActivityTagsBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.utils.ThemeWrapper;

import java.util.List;

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
public class TagsActivity extends AppCompatActivity {

    private ActivityTagsBinding binding;

    TagListAdapter listAdapter;

    protected TagService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        binding = ActivityTagsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = new TagService(((KinoApplication) getApplication()).getDaoSession());

        binding.fabTags.setOnClickListener(v -> startEditTagActivity());

        setSupportActionBar(binding.tagsToolbar.toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);
            actionbar.setTitle(R.string.tags_title);
            actionbar.setSubtitle(R.string.app_name);
        }

        configureDrawer();
    }

    private void fetchAndSetTags() {
        List<TagDto> dataDtos = service.getAll();

        listAdapter = new TagListAdapter(this, dataDtos, service);
        binding.tagList.setAdapter(listAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO should it reuse adapter list ?
        fetchAndSetTags();
    }

    private void configureDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_tags);

        navigationView.setNavigationItemSelectedListener(this::navigate);
    }

    private boolean navigate(MenuItem menuItem) {
        binding.drawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.nav_reviews:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_wishlist:
                startActivity(
                        new Intent(getApplicationContext(), WishlistActivity.class)
                );
                break;
        }

        binding.fabTags.setOnClickListener(v -> startEditTagActivity());
        return true;
    }

    private void startEditTagActivity() {
        Intent intent = new Intent(getApplicationContext(), EditTag.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_export:
                startActivity(new Intent(this, ExportDb.class));
                return true;
            case R.id.action_import:
                startActivity(new Intent(this, ImportInDb.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case android.R.id.home:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}