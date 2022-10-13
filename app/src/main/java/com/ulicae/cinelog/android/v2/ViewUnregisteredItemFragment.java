package com.ulicae.cinelog.android.v2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.ViewUnregisteredKino;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.ActivityViewUnregisteredKinoBinding;
import com.ulicae.cinelog.databinding.ContentKinoViewUnregisteredBinding;

import org.parceler.Parcels;

public class ViewUnregisteredItemFragment extends Fragment {
    KinoDto kino;
    int position;
    boolean editted = false;

    private ActivityViewUnregisteredKinoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityViewUnregisteredKinoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        binding.fab.setOnClickListener(fabView -> ((ViewUnregisteredKino)requireActivity()).goToEditReview(kino));

        kino = Parcels.unwrap(requireActivity().getIntent().getParcelableExtra("kino"));
        position = requireActivity().getIntent().getIntExtra("kino_position", -1);

        bindData();
        configureLabels(requireActivity().getIntent().getStringExtra("dtoType"));
        initToolbar();
    }

    private void initToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.viewUnregisteredToolbar.toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindData() {
        ContentKinoViewUnregisteredBinding viewUnregisteredContent = binding.viewUnregisteredContent;

        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(getActivity())
                    .load("https://image.tmdb.org/t/p/w185" + kino.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(viewUnregisteredContent.viewKinoTmdbImageLayout);
        }
        viewUnregisteredContent.viewKinoTmdbYear.setText(kino.getReleaseDate());
        viewUnregisteredContent.viewKinoTmdbOverview.setText(kino.getOverview());

        viewUnregisteredContent.viewKinoTmdbTitle.setText(kino.getTitle());
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
