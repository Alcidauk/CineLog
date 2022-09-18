package com.ulicae.cinelog.android.activities;

import static com.ulicae.cinelog.android.activities.ViewKino.RESULT_ADD_REVIEW;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.fragments.serie.SerieViewEpisodesFragment;
import com.ulicae.cinelog.android.activities.fragments.serie.SerieViewGeneralFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.databinding.ActivityViewSerieBinding;
import com.ulicae.cinelog.utils.ThemeWrapper;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class ViewSerie extends AppCompatActivity {

    private SerieViewGeneralFragment generalFragment;

    KinoDto kino;
    int position;
    boolean editted = false;

    private ActivityViewSerieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ThemeWrapper().setThemeWithPreferences(this);

        binding = ActivityViewSerieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditReview.class);
            intent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
            intent.putExtra("kino", Parcels.wrap(kino));
            startActivityForResult(intent, RESULT_ADD_REVIEW);
        });

        kino = Parcels.unwrap(getIntent().getParcelableExtra("kino"));
        position = getIntent().getIntExtra("kino_position", -1);

        setSupportActionBar(binding.viewSerieToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setViewPager(binding.viewSerieContent.serieViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (editted) {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("dtoType", getIntent().getStringExtra("dtoType"));
                returnIntent.putExtra("kino", Parcels.wrap(kino));
                returnIntent.putExtra("kino_position", position);
                setResult(Activity.RESULT_OK, returnIntent);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_REVIEW && resultCode == Activity.RESULT_OK) {
            kino = Parcels.unwrap(data.getParcelableExtra("kino"));
            generalFragment.updateKino((SerieDto) kino);
        }
    }

    private void setViewPager(ViewPager viewPager) {
        ViewSerie.ViewPagerAdapter adapter = new ViewSerie.ViewPagerAdapter(getSupportFragmentManager());

        generalFragment = new SerieViewGeneralFragment();
        adapter.addFragment(generalFragment, getString(R.string.title_fragment_serie_general));
        adapter.addFragment(new SerieViewEpisodesFragment(), getString(R.string.title_fragment_serie_episodes));

        viewPager.setAdapter(adapter);
        binding.viewSerieToolbar.tabs.setupWithViewPager(viewPager);
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
