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

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.TagsActivity;
import com.ulicae.cinelog.android.activities.add.AddKinoFragment;
import com.ulicae.cinelog.android.activities.add.AddSerieActivity;
import com.ulicae.cinelog.android.settings.SettingsActivity;
import com.ulicae.cinelog.databinding.ActivityMainBinding;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;
import com.ulicae.cinelog.utils.ThemeWrapper;
import com.ulicae.cinelog.utils.UpgradeFixRunner;

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


        // ELEMENT ADDITION
        // TODO give callback to adapter
        //binding.fab.setOnClickListener(v ->
               // addElement(
              //          ((ViewSerieFragment.ViewPagerAdapter) binding.categoryPager.getAdapter()).getItem(binding.categoryPager.getCurrentItem())
               // )
        //);


    private void checkNeededFix() {
        new UpgradeFixRunner(getBaseContext(), getApplication()).runFixesIfNeeded();
    }

   /* public void navigateToMovieWishlist() {
        Fragment fragment = new WishlistFragment();

        binding.toolbarNew.toolbar.setSubtitle(R.string.app_name);

        getSupportFragmentManager().beginTransaction()
                //.addToBackStack("MovieWishlist")
                .replace(R.id.nav_host_fragment, fragment, "MovieWishlist")
                .commit();
    }*/


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
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    public void goToKinoReviewCreation(){
        Fragment fragment = new AddKinoFragment();

        getSupportFragmentManager().beginTransaction()
                //.addToBackStack("MovieWishlist")
                .replace(R.id.nav_host_fragment, fragment, "MovieWishlist")
                .commit();
    }

    public void goToSerieReviewCreation(){
        Intent intent = new Intent(getApplicationContext(), AddSerieActivity.class);
        intent.putExtra("toWishlist", false);
        startActivity(intent);
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




    /* TODO rewrite

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        //adapter.addFragment(new MovieFragment(), getString(R.string.title_fragment_movie));
        //adapter.addFragment(new SerieFragment(), getString(R.string.title_fragment_serie));

        binding.categoryPager.setAdapter(adapter);
        binding.mainToolbar.tabs.setupWithViewPager(binding.categoryPager);
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
     */
}