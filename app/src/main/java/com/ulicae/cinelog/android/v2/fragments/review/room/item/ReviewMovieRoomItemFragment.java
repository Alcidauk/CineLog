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
import com.ulicae.cinelog.databinding.FragmentReviewMovieItemBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemKinoBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemReviewBinding;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ReviewMovieRoomItemFragment extends ShareableFragment<KinoDto> {

    private FragmentReviewMovieItemBinding binding;

    private ReviewAsyncService reviewService;

    int position;

    private List<Disposable> disposables;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.addOptionMenu();
        this.disposables = new ArrayList<>();
        binding = FragmentReviewMovieItemBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        long itemId = requireArguments().getLong("review_id");
        position = requireArguments().getInt("kino_position", -1);

        reviewService = new ReviewAsyncService((KinoApplication) requireActivity().getApplication(), ItemEntityType.MOVIE);

        disposables.add(
        reviewService.findById(itemId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        review -> {
                            item = review;

                            LayoutReviewItemKinoBinding viewKinoContentLayout = binding.viewKinoContentLayout;
                            LayoutReviewItemReviewBinding reviewKinoContentLayout = binding.reviewKinoContentLayout;

                            new ReviewItemDataFieldsInflater(item, getActivity(), viewKinoContentLayout, reviewKinoContentLayout).configureFields();

                            ((MainActivity) requireActivity()).getSearchView().setVisibility(View.GONE);
                        },
                        error -> {
                            // TODO
                        }
                )
        );

        setLinkBaseUrl("https://www.themoviedb.org/movie/");

        FloatingActionButton fab = ((MainActivity) requireActivity()).getFab();
        fab.setOnClickListener(
                v -> {
                    Bundle args = new Bundle();
                    args.putString("dtoType", "kino");
                    args.putParcelable("kino", Parcels.wrap(item));
                    args.putBoolean("creation", false);
                    ((MainActivity) requireActivity()).navigateToReview(
                            R.id.action_viewMovieReviewRoomFragment_to_editReviewFragment,
                            args
                    );
                });
        fab.setImageResource(R.drawable.edit_kino);
        fab.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for(Disposable disposable : disposables){
            disposable.dispose();
        }
    }

}
