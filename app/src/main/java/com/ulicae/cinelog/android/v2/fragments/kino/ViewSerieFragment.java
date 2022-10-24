package com.ulicae.cinelog.android.v2.fragments.kino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.fragments.kino.serie.SerieViewEpisodesFragment;
import com.ulicae.cinelog.android.v2.fragments.kino.serie.SerieViewGeneralFragment;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.ContentViewSerieBinding;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ViewSerieFragment extends Fragment {

    private ContentViewSerieBinding binding;

    KinoDto kino;
    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ContentViewSerieBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        kino = Parcels.unwrap(getArguments().getParcelable("kino"));
        position = getArguments().getInt("kino_position", -1);

        binding.fab.setOnClickListener(fabView -> {
            ((MainActivity) requireActivity()).navigateToReview(kino, false);
        });

        setViewPager(binding.serieViewPager);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        Bundle args = new Bundle();
        args.putParcelable("kino", Parcels.wrap(kino));

        SerieViewGeneralFragment generalFragment = new SerieViewGeneralFragment();
        generalFragment.setArguments(args);
        adapter.addFragment(generalFragment, getString(R.string.title_fragment_serie_general));

        SerieViewEpisodesFragment episodesFragment = new SerieViewEpisodesFragment();
        episodesFragment.setArguments(args);
        adapter.addFragment(episodesFragment, getString(R.string.title_fragment_serie_episodes));

        viewPager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(viewPager);
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


  /*  TODO rewrite state management to get right data from editreview

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
    }*/
}
