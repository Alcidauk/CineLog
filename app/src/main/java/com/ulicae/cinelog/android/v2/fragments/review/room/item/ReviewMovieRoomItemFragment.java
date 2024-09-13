package com.ulicae.cinelog.android.v2.fragments.review.room.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.android.v2.fragments.ShareableFragment;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.room.services.ReviewService;
import com.ulicae.cinelog.databinding.FragmentReviewMovieItemBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemKinoBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemReviewBinding;

import org.parceler.Parcels;

public class ReviewMovieRoomItemFragment extends ShareableFragment<KinoDto> {

    private FragmentReviewMovieItemBinding binding;

    private ReviewService reviewService;

    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.addOptionMenu();
        binding = FragmentReviewMovieItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        int itemId = requireArguments().getInt("review_id");
        position = requireArguments().getInt("kino_position", -1);

        reviewService = new ReviewService(((KinoApplication) requireActivity().getApplication()).getDb());
        item = reviewService.getWithId(itemId);

        setLinkBaseUrl("https://www.themoviedb.org/movie/");

        LayoutReviewItemKinoBinding viewKinoContentLayout = binding.viewKinoContentLayout;
        LayoutReviewItemReviewBinding reviewKinoContentLayout = binding.reviewKinoContentLayout;

        new ReviewItemDataFieldsInflater(item, getActivity(), viewKinoContentLayout, reviewKinoContentLayout).configureFields();

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> {
                    Bundle args = new Bundle();
                    args.putString("dtoType", "kino");
                    args.putParcelable("kino", Parcels.wrap(item));
                    args.putBoolean("creation", false);
                    ((MainActivity) requireActivity()).navigateToReview(
                            R.id.action_viewKinoFragment_to_editReviewFragment,
                            args
                    );
                });
        fab.setImageResource(R.drawable.edit_kino);
        fab.show();

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
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
