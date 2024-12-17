package com.ulicae.cinelog.android.v2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.databinding.LayoutKinoItemBinding;

import org.parceler.Parcels;

public class ViewUnregisteredItemFragment extends Fragment {
    KinoDto kino;
    int position;

    private LayoutKinoItemBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LayoutKinoItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> {
                    Bundle args = new Bundle();
                    args.putString("dtoType", kino instanceof SerieDto ? "serie" : "kino");
                    args.putParcelable("kino", Parcels.wrap(kino));
                    args.putBoolean("creation", true);
                    ((MainActivity) requireActivity()).navigateToReview(
                            R.id.action_viewUnregisteredItemFragment_to_editReviewFragment,
                            args);
                }
        );
        fab.setImageResource(R.drawable.add_review);
        fab.show();

        ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);

        kino = Parcels.unwrap(requireArguments().getParcelable("kino"));
        position = requireArguments().getInt("kino_position", -1);
        String dtoType = requireArguments().getString("dtoType");

        bindData();
        configureLabels(dtoType);
    }

    private void bindData() {
        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(getActivity())
                    .load("https://image.tmdb.org/t/p/w185" + kino.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(binding.viewKinoTmdbImageLayout);
        }
        binding.viewKinoTmdbYear.setText(kino.getReleaseDate());
        binding.viewKinoTmdbOverview.setText(kino.getOverview());

        binding.viewKinoTmdbTitle.setText(kino.getTitle());
    }

    private void configureLabels(String dtoType) {
        if (dtoType.equals("serie")) {
            requireActivity().setTitle(R.string.title_activity_view_unregistered_serie);
        }
    }

    /*  TODO rewrite state management to get right data from editreview

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                kino = Parcels.unwrap(data.getParcelableExtra("kino"));
                editted = true;
                System.out.println("Result Ok");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("Result Cancelled");
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
