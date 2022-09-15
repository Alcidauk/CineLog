package com.ulicae.cinelog.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.add.AddKino;
import com.ulicae.cinelog.android.activities.add.AddSerieActivity;
import com.ulicae.cinelog.android.activities.fragments.reviews.MovieFragment;
import com.ulicae.cinelog.android.activities.fragments.reviews.SerieFragment;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.databinding.ActivityMainBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.ulicae.cinelog.utils.UpgradeFixRunner;

import java.util.ArrayList;
import java.util.List;

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

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.mainToolbar.toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);
            actionbar.setTitle(R.string.toolbar_title_reviews);
            actionbar.setSubtitle(R.string.app_name);
        }

        setViewPager(binding.categoryPager);
        binding.mainToolbar.tabs.setupWithViewPager(binding.categoryPager);

        binding.fab.setOnClickListener(v -> setReviewFragment());

        configureDrawer();

        checkNeededFix();
    }

    private void configureDrawer() {
        binding.navView.setCheckedItem(R.id.nav_reviews);
        binding.navView.setNavigationItemSelectedListener(this::navigate);
    }

    private boolean navigate(MenuItem menuItem) {
        // close drawer when item is tapped
        binding.drawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.nav_wishlist:
                startActivity(
                        new Intent(getApplicationContext(), WishlistActivity.class)
                );
                break;
            case R.id.nav_tags:
                startActivity(
                        new Intent(getApplicationContext(), TagsActivity.class));
                break;
        }

        setViewPager(binding.categoryPager);

        binding.fab.setOnClickListener(v -> setReviewFragment());

        return true;
    }

    private void setReviewFragment() {
        Fragment fragment = ((ViewPagerAdapter) binding.categoryPager.getAdapter()).getItem(binding.categoryPager.getCurrentItem());

        Intent intent;
        if (fragment instanceof MovieFragment) {
            intent = new Intent(getApplicationContext(), AddKino.class);
        } else {
            intent = new Intent(getApplicationContext(), AddSerieActivity.class);
        }

        intent.putExtra("toWishlist", false);

        startActivity(intent);
    }

    private void checkNeededFix() {
        new UpgradeFixRunner(getBaseContext(), getApplication()).runFixesIfNeeded();
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

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), getString(R.string.title_fragment_movie));
        adapter.addFragment(new SerieFragment(), getString(R.string.title_fragment_serie));
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}