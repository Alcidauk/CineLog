package com.ulicae.cinelog.android.v2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.TagsActivity;
import com.ulicae.cinelog.android.v2.fragments.wishlist.item.WishlistItemFragment;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.android.v2.ViewUnregisteredItemFragment;
import com.ulicae.cinelog.android.v2.fragments.review.item.ReviewMovieItemFragment;
import com.ulicae.cinelog.android.v2.fragments.review.item.ReviewSerieItemFragment;
import com.ulicae.cinelog.android.v2.fragments.tmdbsearch.SearchTmbdSerieFragment;
import com.ulicae.cinelog.android.v2.fragments.tmdbsearch.SearchTmdbMovieFragment;
import com.ulicae.cinelog.android.v2.fragments.review.edit.ReviewEditionFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.databinding.ActivityMainBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.ulicae.cinelog.utils.UpgradeFixRunner;

import org.parceler.Parcels;

import java.util.HashSet;
import java.util.Set;

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
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavigation();

        checkNeededFix();
    }

    private void checkNeededFix() {
        new UpgradeFixRunner(getBaseContext(), getApplication()).runFixesIfNeeded();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNavigation() {
        setSupportActionBar(binding.toolbarNew.toolbar);
        binding.toolbarNew.toolbar.setSubtitle(R.string.app_name);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        Set<Integer> topLevelDestinations = new HashSet<Integer>(){{
            add(R.id.nav_reviews_movie);
            add(R.id.nav_reviews_serie);
            add(R.id.nav_wishlist_movie);
            add(R.id.nav_wishlist_serie);
            add(R.id.nav_tags);
        }};
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this,
                this.navController,
                this.appBarConfiguration);

        // needed to bind drawer layout with navigation
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, binding.drawerLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            goToExport();
            return true;
        } else if (item.getItemId() == R.id.action_import) {
            goToImport();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            goToSettings();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    public void goToTmdbMovieSearch(boolean wishlist){
        Fragment fragment = new SearchTmdbMovieFragment();

        Bundle args = new Bundle();
        args.putBoolean("toWishlist", wishlist);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("MovieReviewList")
                .replace(R.id.nav_host_fragment, fragment, "MovieReviewCreation")
                .commit();
    }

    public void goToTmdbSerieSearch(boolean wishlist){
        Fragment fragment = new SearchTmbdSerieFragment();

        Bundle args = new Bundle();
        args.putBoolean("toWishlist", wishlist);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("SerieReviewList")
                .replace(R.id.nav_host_fragment, fragment, "SerieReviewCreation")
                .commit();
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

    public void navigateToItem(KinoDto kinoDto, int position, boolean inDb) {
        if (inDb) {
            Fragment fragment;
            Bundle args = new Bundle();
            if(kinoDto instanceof SerieDto){
                fragment = new ReviewSerieItemFragment();
                args.putString("dtoType", "serie");
            } else {
                fragment = new ReviewMovieItemFragment();
                args.putString("dtoType", "kino");
            }

            args.putParcelable("kino", Parcels.wrap(kinoDto));
            args.putInt("kino_position", position);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack("ViewKino")
                    .replace(R.id.nav_host_fragment, fragment, "ViewKino")
                    .commit();
        } else {
            Fragment fragment = new ViewUnregisteredItemFragment();

            Bundle args = new Bundle();
            args.putString("dtoType", kinoDto instanceof SerieDto ? "serie" : "kino");
            args.putParcelable("kino", Parcels.wrap(kinoDto));
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .addToBackStack("ViewUnregisteredKino")
                    .replace(R.id.nav_host_fragment, fragment, "ViewUnregisteredKino")
                    .commit();
        }
    }

    public void navigateToReview(KinoDto kinoDto, boolean creation) {
        Fragment fragment = new ReviewEditionFragment();

        Bundle args = new Bundle();
        args.putString("dtoType", kinoDto instanceof SerieDto ? "serie" : "kino");
        args.putParcelable("kino", Parcels.wrap(kinoDto));
        args.putBoolean("creation", creation);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("EditReview")
                .replace(R.id.nav_host_fragment, fragment, "EditReview")
                .commit();
    }

    public void navigateToWishlistItem(WishlistDataDto dataDto) {
        WishlistItemFragment fragment = new WishlistItemFragment();

        Bundle args = new Bundle();
        args.putParcelable("dataDto", Parcels.wrap(dataDto));
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("WishlistItem")
                .replace(R.id.nav_host_fragment, fragment, "WishlistItem")
                .commit();
    }

    public FloatingActionButton getFab() {
        return binding.fab;
    }
}