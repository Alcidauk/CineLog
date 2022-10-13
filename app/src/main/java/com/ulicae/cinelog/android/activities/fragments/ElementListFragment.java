package com.ulicae.cinelog.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.MainActivity;
import com.ulicae.cinelog.android.activities.fragments.reviews.MovieFragment;
import com.ulicae.cinelog.android.activities.fragments.reviews.SerieFragment;
import com.ulicae.cinelog.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class ElementListFragment extends Fragment {

    private ActivityMainBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        initToolbar();
        initViewPager();
        initNavigation();

        // ELEMENT ADDITION
        binding.fab.setOnClickListener(v ->
                ((MainActivity) requireActivity()).addElement(
                        ((ViewPagerAdapter) binding.categoryPager.getAdapter()).getItem(binding.categoryPager.getCurrentItem())
                )
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            ((MainActivity) requireActivity()).goToExport();
        } else if (item.getItemId() == R.id.action_import) {
            ((MainActivity) requireActivity()).goToImport();
        } else if (item.getItemId() == R.id.action_settings) {
            ((MainActivity) requireActivity()).goToSettings();
        } else if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initNavigation() {
        binding.navView.setCheckedItem(R.id.nav_reviews);
        binding.navView.setNavigationItemSelectedListener(this::navigateToActivity);
    }

    private boolean navigateToActivity(MenuItem menuItem) {
        // close drawer when item is tapped
        binding.drawerLayout.closeDrawers();

        if (menuItem.getItemId() == R.id.nav_wishlist) {
            ((MainActivity) requireActivity()).goToWishlist();
        } else if (menuItem.getItemId() == R.id.nav_tags) {
            ((MainActivity) requireActivity()).goToTags();
        } else {
            return false;
        }

        initViewPager();
        return true;
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mainToolbar.toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);
            actionbar.setTitle(R.string.toolbar_title_reviews);
            actionbar.setSubtitle(R.string.app_name);
        }
    }

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), getString(R.string.title_fragment_movie));
        adapter.addFragment(new SerieFragment(), getString(R.string.title_fragment_serie));

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

}
