package com.ulicae.cinelog.android.v2;

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
import androidx.viewpager.widget.ViewPager;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.WishlistActivity;
import com.ulicae.cinelog.android.activities.fragments.wishlist.MovieWishlistFragment;
import com.ulicae.cinelog.android.activities.fragments.wishlist.SerieWishlistFragment;
import com.ulicae.cinelog.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

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
        //setViewPager(binding.categoryPager);

        initFab();
        initToolbar();
        initNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            ((WishlistActivity) requireActivity()).goToSettings();
        } else if (item.getItemId() == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFab() {
        // TODO binding.fab.setOnClickListener(v -> {
        //    ((WishlistActivity) requireActivity()).goToReview(getCurrentFragmentItem());
        //});
    }

    @NonNull
    private Fragment getCurrentFragmentItem() {
        return null; // TODO ((ViewPagerAdapter) binding.categoryPager.getAdapter())
               // .getItem(binding.categoryPager.getCurrentItem());
    }

    private void initNavigation() {
        binding.navView.setCheckedItem(R.id.nav_wishlist);
        binding.navView.setNavigationItemSelectedListener(this::navigateToActivity);
    }

    private void initToolbar() {
        // ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.mainToolbar.toolbar);
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);
            actionbar.setTitle(R.string.toolbar_title_wishlist);
            actionbar.setSubtitle(R.string.app_name);
        }
    }

    private boolean navigateToActivity(MenuItem menuItem) {
        // close drawer when item is tapped
        binding.drawerLayout.closeDrawers();

        if (menuItem.getItemId() == R.id.nav_reviews) {
            ((WishlistActivity) requireActivity()).goToReviews();
        } else if (menuItem.getItemId() == R.id.nav_tags) {
            ((WishlistActivity) requireActivity()).goToTags();
        } else {
            return false;
        }

        return true;
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        adapter.addFragment(new MovieWishlistFragment(), getString(R.string.title_fragment_wishlist_movie));
        adapter.addFragment(new SerieWishlistFragment(), getString(R.string.title_fragment_wishlist_serie));

        viewPager.setAdapter(adapter);
       // TODO binding.mainToolbar.tabs.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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
