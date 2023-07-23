package com.ulicae.cinelog.android.v2.fragments.review.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.LayoutReviewItemKinoBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemReviewBinding;
import com.ulicae.cinelog.databinding.FragmentReviewMovieItemBinding;

import org.parceler.Parcels;

public class ReviewMovieItemFragment extends Fragment {

    private FragmentReviewMovieItemBinding binding;

    KinoDto kino;
    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewMovieItemBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true/*kino != null && kino.getTmdbKinoId()!=null*/);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        kino = Parcels.unwrap(requireArguments().getParcelable("kino"));
        position = requireArguments().getInt("kino_position", -1);

        LayoutReviewItemKinoBinding viewKinoContentLayout = binding.viewKinoContentLayout;
        LayoutReviewItemReviewBinding reviewKinoContentLayout = binding.reviewKinoContentLayout;

        new ReviewItemDataFieldsInflater(kino, getActivity(), viewKinoContentLayout, reviewKinoContentLayout).configureFields();

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> ((MainActivity) requireActivity()).navigateToReview(kino, false, R.id.action_viewKinoFragment_to_editReviewFragment)
        );
        fab.setImageResource(R.drawable.edit_kino);
        fab.show();

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_review, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareMovie();
            return true;
        }
        return true;
    }

    private void shareMovie() {
        if (this.kino.getTmdbKinoId()==null) {
            shareText(this.kino.getTitle());
        } else {
            shareText("https://www.themoviedb.org/movie/" + this.kino.getTmdbKinoId());
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

  /*  TODO rewrite state management to get right data from editreview

  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                kino = Parcels.unwrap(data.getParcelableExtra("kino"));
                editted = true;
            }
        }
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
    }*/
}
