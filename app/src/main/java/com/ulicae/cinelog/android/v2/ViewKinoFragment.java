package com.ulicae.cinelog.android.v2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.android.activities.view.ViewDataFieldsInflater;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.ActivityViewKinoBinding;
import com.ulicae.cinelog.databinding.ContentKinoViewBinding;
import com.ulicae.cinelog.databinding.ContentReviewViewBinding;

import org.parceler.Parcels;

import java.util.Objects;

public class ViewKinoFragment extends Fragment {

    private ActivityViewKinoBinding binding;

    KinoDto kino;
    int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityViewKinoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        kino = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("kino"));
        position = getActivity().getIntent().getIntExtra("kino_position", -1);

        ContentKinoViewBinding viewKinoContentLayout = binding.viewKinoContent.viewKinoContentLayout;
        ContentReviewViewBinding reviewKinoContentLayout = binding.viewKinoContent.reviewKinoContentLayout;

        new ViewDataFieldsInflater(kino, getActivity(), viewKinoContentLayout, reviewKinoContentLayout).configureFields();

        binding.fab.setOnClickListener(fabView -> {
            ((ViewKino) requireActivity()).goToKinoEdition(kino);
        });
        initToolbar();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.viewKinoToolbar.toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
