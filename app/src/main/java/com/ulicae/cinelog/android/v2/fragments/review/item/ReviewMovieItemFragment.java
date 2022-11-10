package com.ulicae.cinelog.android.v2.fragments.review.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.ContentKinoViewBinding;
import com.ulicae.cinelog.databinding.ContentReviewViewBinding;
import com.ulicae.cinelog.databinding.ContentViewKinoBinding;

import org.parceler.Parcels;

public class ReviewMovieItemFragment extends Fragment {

    private ContentViewKinoBinding binding;

    KinoDto kino;
    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ContentViewKinoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        kino = Parcels.unwrap(requireArguments().getParcelable("kino"));
        position = requireArguments().getInt("kino_position", -1);

        ContentKinoViewBinding viewKinoContentLayout = binding.viewKinoContentLayout;
        ContentReviewViewBinding reviewKinoContentLayout = binding.reviewKinoContentLayout;

        new ReviewItemDataFieldsInflater(kino, getActivity(), viewKinoContentLayout, reviewKinoContentLayout).configureFields();

        binding.fab.setOnClickListener(fabView -> {
            ((MainActivity) requireActivity()).navigateToReview(kino, false);
        });
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
