package com.ulicae.cinelog.android.v2.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.BuildConfig;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.databinding.ActivityMainBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.ulicae.cinelog.utils.UpgradeFixRunner;

import org.parceler.Parcels;

import java.util.HashSet;
import java.util.Set;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
    private UpgradeFixRunner upgradeFixRunner;

    private AppDatabase db;

    public AppDatabase getDb() {
        if(this.db == null) {
            this.db = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            AppDatabase.class,
                            "database-cinelog")
                    .build();
        }
        return this.db;
    }

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
        upgradeFixRunner = new UpgradeFixRunner(getBaseContext(), getApplication());
        upgradeFixRunner.runFixesIfNeeded();
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

        Set<Integer> topLevelDestinations = new HashSet<Integer>() {{
            add(R.id.nav_reviews_movie);
            add(R.id.nav_reviews_serie);
            add(R.id.nav_wishlist_movie);
            add(R.id.nav_wishlist_serie);
            add(R.id.nav_reviews_room_movie);
            add(R.id.nav_reviews_room_serie);
            add(R.id.nav_wishlist_room_movie);
            add(R.id.nav_wishlist_room_serie);
            add(R.id.nav_room_tags);
        }};
        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this,
                this.navController,
                this.appBarConfiguration);

        // needed to bind drawer layout with navigation
        NavigationUI.setupWithNavController(binding.navView, navController);

        listenDrawerOpenal();
    }

    private void listenDrawerOpenal() {
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                ((TextView) findViewById(R.id.androidVersion)).setText("v " + BuildConfig.VERSION_NAME);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
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

    public void goToTmdbMovieSearch(boolean wishlist) {
        Bundle args = new Bundle();
        args.putBoolean("toWishlist", wishlist);

        if (wishlist) {
            navController.navigate(R.id.action_nav_wishlist_movie_to_searchTmdbMovieFragment, args);
        } else {
            navController.navigate(R.id.action_nav_reviews_movie_to_searchTmdbMovieFragment, args);
        }
    }

    public void goToTmdbSerieSearch(boolean wishlist) {
        Bundle args = new Bundle();
        args.putBoolean("toWishlist", wishlist);

        if (wishlist) {
            navController.navigate(R.id.action_nav_wishlist_serie_to_searchTmbdSerieFragment, args);
        } else {
            navController.navigate(R.id.action_nav_reviews_serie_to_searchTmbdSerieFragment, args);
        }
    }

    public void goToTagEdition(TagDto dataDto) {
        Bundle args = new Bundle();
        if (dataDto != null) {
            args.putParcelable("tag", Parcels.wrap(dataDto));
        }
        navController.navigate(R.id.action_nav_tags_to_editTagFragment, args);
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

    public void navigateToItem(KinoDto kinoDto, int position, boolean inDb, boolean fromSearch, boolean room) {
        Bundle args = new Bundle();
        int action = determineAction(kinoDto, inDb, fromSearch, room);
        if (inDb) {
            args.putInt("review_id", Math.toIntExact(kinoDto.getId()));
            args.putInt("kino_position", position);
        }

        // TODO remove kino, not used after room migration
        args.putParcelable("kino", Parcels.wrap(kinoDto));
        args.putString("dtoType", kinoDto instanceof SerieDto ? "serie" : "kino");

        navController.navigate(action, args);
    }

    // TODO better and without class check
    public int determineAction(KinoDto kinoDto, boolean inDb, boolean fromSearch, boolean room) {
        if (room) {
            if(inDb) {
                // TODO toutes actions vers room fragments
                if(kinoDto instanceof SerieDto) {
                    return fromSearch ?
                            R.id.action_searchTmbdSerieFragment_to_viewSerieFragment :
                            R.id.action_nav_reviews_room_serie_to_viewSerieRoomFragment;
                } else {
                    return fromSearch ?
                            R.id.action_searchTmdbMovieFragment_to_viewKinoFragment :
                            R.id.action_nav_reviews_room_movie_to_viewReviewFragment;
                }
            } else {
                return kinoDto instanceof SerieDto ?
                        R.id.action_searchTmbdSerieFragment_to_viewUnregisteredItemFragment :
                        R.id.action_searchTmdbMovieFragment_to_viewUnregisteredItemFragment;
            }
        } else {
            if(inDb) {
                if(kinoDto instanceof SerieDto) {
                    return fromSearch ?
                            R.id.action_searchTmbdSerieFragment_to_viewSerieFragment :
                            R.id.action_nav_reviews_serie_to_viewSerieFragment;
                } else {
                    return fromSearch ?
                            R.id.action_searchTmdbMovieFragment_to_viewKinoFragment :
                            R.id.action_nav_reviews_movie_to_viewKinoFragment;
                }
            } else {
                return kinoDto instanceof SerieDto ?
                        R.id.action_searchTmbdSerieFragment_to_viewUnregisteredItemFragment :
                        R.id.action_searchTmdbMovieFragment_to_viewUnregisteredItemFragment;
            }
        }
    }

    public void navigateToReview(KinoDto kinoDto, boolean creation, int action) {
        Bundle args = new Bundle();
        args.putString("dtoType", kinoDto instanceof SerieDto ? "serie" : "kino");
        args.putParcelable("kino", Parcels.wrap(kinoDto));
        args.putBoolean("creation", creation);

        navController.navigate(action, args);
    }

    public void navigateToWishlistItem(WishlistDataDto dataDto, int action) {
        Bundle args = new Bundle();
        args.putParcelable("dataDto", Parcels.wrap(dataDto));
        navController.navigate(action, args);
    }

    public FloatingActionButton getFab() {
        return binding.fab;
    }

    public SearchView getSearchView() {
        return binding.toolbarNew.searchBar;
    }

    public void navigateBackToWishlist(WishlistItemType type) {
        navController.navigate(
                type == WishlistItemType.SERIE ?
                        R.id.action_wishlistItemFragment_to_nav_wishlist_serie :
                        R.id.action_wishlistItemFragment_to_nav_wishlist_movie
        );
    }

    public void navigateBackToReviewList(KinoDto fromKinoDto) {
        navController.navigate(
                fromKinoDto instanceof SerieDto ?
                        R.id.action_editReviewFragment_to_nav_reviews_serie :
                        R.id.action_editReviewFragment_to_nav_reviews_movie
        );
    }

    public void navigateBack() {
        navController.popBackStack();
    }

    @Override
    public void onDestroy() {
        if(this.upgradeFixRunner != null){
            this.upgradeFixRunner.clear();
        }
        super.onDestroy();
    }
}