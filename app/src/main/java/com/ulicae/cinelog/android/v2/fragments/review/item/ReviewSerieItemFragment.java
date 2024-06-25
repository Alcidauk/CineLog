package com.ulicae.cinelog.android.v2.fragments.review.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.android.v2.fragments.ShareableFragment;
import com.ulicae.cinelog.android.v2.fragments.review.item.serie.SerieViewEpisodesFragment;
import com.ulicae.cinelog.android.v2.fragments.review.item.serie.SerieViewGeneralFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.databinding.FragmentReviewSerieItemBinding;

import org.parceler.Parcels;

public class ReviewSerieItemFragment extends ShareableFragment<SerieDto> {

    private FragmentReviewSerieItemBinding binding;

    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.addOptionMenu();
        binding = FragmentReviewSerieItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        item = Parcels.unwrap(getArguments().getParcelable("kino"));
        position = getArguments().getInt("kino_position", -1);

        setLinkBaseUrl("https://www.themoviedb.org/tv/");

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> {
                    Bundle args = new Bundle();
                    args.putString("dtoType", "serie");
                    args.putParcelable("kino", Parcels.wrap(item));
                    args.putBoolean("creation", false);
                    ((MainActivity) requireActivity()).navigateToReview(
                            R.id.action_viewSerieFragment_to_editReviewFragment,
                            args
                    );
                });
        fab.setImageResource(R.drawable.edit_kino);
        fab.show();

        setViewPager();
    }

    private void setViewPager() {
        SerieItemPagerAdapter adapter = new SerieItemPagerAdapter(requireActivity(), item);

        binding.serieViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabs, binding.serieViewPager,
                (tab, position) -> tab.setText(position == 0 ?
                        getString(R.string.title_fragment_serie_general) :
                        getString(R.string.title_fragment_serie_episodes))
        ).attach();
    }

    private class SerieItemPagerAdapter extends FragmentStateAdapter {
        private final KinoDto kino;

        public SerieItemPagerAdapter(FragmentActivity fa, KinoDto kino) {
            super(fa);
            this.kino = kino;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Bundle args = new Bundle();
            args.putParcelable("kino", Parcels.wrap(kino));

            Fragment fragment = position == 0 ? new SerieViewGeneralFragment() : new SerieViewEpisodesFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 2;
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
