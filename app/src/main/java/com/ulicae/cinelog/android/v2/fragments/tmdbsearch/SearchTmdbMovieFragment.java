package com.ulicae.cinelog.android.v2.fragments.tmdbsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.fragments.review.add.TmdbMovieResultsAdapter;
import com.ulicae.cinelog.android.v2.fragments.wishlist.add.WishlistMovieResultsAdapter;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.databinding.FragmentSearchTmdbBinding;
import com.ulicae.cinelog.network.task.MovieNetworkTaskCreator;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;

/**
 * CineLog Copyright 2022 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
public class SearchTmdbMovieFragment extends SearchTmdbFragment<BaseMovie> {

    private boolean toWishlist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentSearchTmdbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toWishlist = requireArguments().getBoolean("toWishlist", false);

        networkTaskManager = new NetworkTaskManager(this, new MovieNetworkTaskCreator());
        KinoApplication application = (KinoApplication) getActivity().getApplication();
        dataService = new KinoService(
                application.getDaoSession(),
                application.getDb()
        );
    }

    @Override
    protected void executeTask(String textToSearch) {
        Call<MovieResultsPage> search = tmdbServiceWrapper.search(binding.kinoSearch.getText().toString());
        networkTaskManager.createAndExecute(search);
    }

    @Override
    public void onFromScratchClick(View view) {
        // TODO should be callbacks ?
        if (!toWishlist) {
            KinoDto kinoToCreate = new KinoDto();
            kinoToCreate.setTitle(binding.kinoSearch.getText().toString());

            Bundle args = new Bundle();
            args.putString("dtoType", "kino");
            args.putParcelable("kino", Parcels.wrap(kinoToCreate));
            args.putBoolean("creation", true);
            ((MainActivity) requireActivity()).navigateToReview(
                    R.id.action_searchTmdbMovieFragment_to_editReviewFragment,
                    args
            );
        } else {
            ((MainActivity) requireActivity()).navigateToWishlistItem(
                            new WishlistDataDto(
                                    binding.kinoSearch.getText().toString(),
                                    WishlistItemType.MOVIE
                            ),
                    R.id.action_searchTmdbMovieFragment_to_wishlistItemFragment
            );
        }
    }

    public void populateListView(final List<BaseMovie> movies) {
        ArrayAdapter<BaseMovie> arrayAdapter;
        if (!toWishlist) {
            arrayAdapter = new TmdbMovieResultsAdapter(
                    requireContext(),
                    ((KinoApplication) requireActivity().getApplication()),
                    movies,
                    movieSearchResultClickCallback,
                    movieReviewCreationClickCallback);
        } else {
            arrayAdapter = new WishlistMovieResultsAdapter(
                    requireContext(),
                    movies,
                    wishlistItemCallback);
        }

        binding.kinoResults.setAdapter(arrayAdapter);
        binding.kinoSearchProgressBar.setVisibility(View.GONE);
    }

}