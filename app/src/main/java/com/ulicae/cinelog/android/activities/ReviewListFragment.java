package com.ulicae.cinelog.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.add.AddKino;
import com.ulicae.cinelog.android.activities.add.AddSerieActivity;
import com.ulicae.cinelog.android.activities.fragments.MovieFragment;
import com.ulicae.cinelog.android.activities.fragments.SerieFragment;
import com.ulicae.cinelog.io.exportdb.ExportDb;
import com.ulicae.cinelog.io.importdb.ImportInDb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewListFragment extends Fragment {

    @BindView(R.id.category_tabs)
    TabLayout tabLayout;
    @BindView(R.id.category_pager)
    ViewPager viewPager;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        ButterKnife.bind(this, view);

        fab = (FloatingActionButton) container.findViewById(R.id.fab);

        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewFragment();
            }
        });

        android.app.ActionBar actionbar = getActivity().getActionBar();
        if (actionbar != null) {
            actionbar.setSubtitle(R.string.toolbar_subtitle_reviews);
        }

        return view;
    }

    private void setReviewFragment() {
        Fragment fragment = ((MainActivity.ViewPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem());

        if (fragment instanceof MovieFragment) {
            Intent intent = new Intent(getContext().getApplicationContext(), AddKino.class);
            startActivity(intent);
        } else if (fragment instanceof SerieFragment) {
            Intent intent = new Intent(getContext().getApplicationContext(), AddSerieActivity.class);
            startActivity(intent);
        }
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new MovieFragment(), getString(R.string.title_fragment_movie));
        adapter.addFragment(new SerieFragment(), getString(R.string.title_fragment_serie));
        viewPager.setAdapter(adapter);
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
