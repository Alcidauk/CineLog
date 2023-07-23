package com.ulicae.cinelog.android.v2.fragments.review.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.ulicae.cinelog.android.v2.fragments.review.item.serie.SerieViewEpisodesFragment;
import com.ulicae.cinelog.android.v2.fragments.review.item.serie.SerieViewGeneralFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.FragmentReviewSerieItemBinding;

import org.parceler.Parcels;

public class ReviewSerieItemFragment extends Fragment {

    private FragmentReviewSerieItemBinding binding;

    KinoDto kino;
    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewSerieItemBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true/*kino != null && kino.getTmdbKinoId()!=null*/);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        kino = Parcels.unwrap(getArguments().getParcelable("kino"));
        position = getArguments().getInt("kino_position", -1);

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> ((MainActivity) requireActivity()).navigateToReview(kino, false, R.id.action_viewSerieFragment_to_editReviewFragment)
        );
        fab.setImageResource(R.drawable.edit_kino);
        fab.show();

        setViewPager();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_review, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareSerie();
            return true;
        }
        return true;
    }

    private void shareSerie() {
        if (this.kino.getTmdbKinoId()==null) {
            shareText(this.kino.getTitle());
        } else {
            shareText("https://www.themoviedb.org/tv/" + this.kino.getTmdbKinoId());
        }
    }
    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void setViewPager() {
        SerieItemPagerAdapter adapter = new SerieItemPagerAdapter(requireActivity(), kino);

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
