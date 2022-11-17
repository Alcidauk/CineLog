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
import com.ulicae.cinelog.android.v2.fragments.review.add.TmdbTvResultsAdapter;
import com.ulicae.cinelog.android.v2.fragments.wishlist.add.WishlistTvResultsAdapter;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.databinding.FragmentSearchTmdbBinding;
import com.ulicae.cinelog.network.task.NetworkTaskManager;
import com.ulicae.cinelog.network.task.TvNetworkTaskCreator;
import com.uwetrottmann.tmdb2.entities.BaseTvShow;
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage;

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
public class SearchTmbdSerieFragment extends SearchTmdbFragment<BaseTvShow> {

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

        networkTaskManager = new NetworkTaskManager(this, new TvNetworkTaskCreator());
        dataService = new SerieService(((KinoApplication) requireActivity().getApplication()).getDaoSession(), requireContext());

        binding.kinoSearchAddFromScratch.setText(getString(R.string.add_serie_from_scratch_label));
        binding.kinoSearch.setHint(getString(R.string.serie_title_hint));
    }

    @Override
    protected void executeTask(String textToSearch) {
        Call<TvShowResultsPage> search = tmdbServiceWrapper.searchTv(binding.kinoSearch.getText().toString());
        networkTaskManager.createAndExecute(search);
    }

    // TODO rewrite navigation
    @Override
    protected void onFromScratchClick(View view) {
        // TODO should be callbacks ?
        if (!toWishlist) {
            SerieDto serieDto = new SerieDto();
            serieDto.setTitle(binding.kinoSearch.getText().toString());

            ((MainActivity) requireActivity()).navigateToReview(serieDto, true, R.id.action_searchTmbdSerieFragment_to_editReviewFragment);
        } else {
            ((MainActivity) requireActivity()).navigateToWishlistItem(
                    new WishlistDataDto(
                            binding.kinoSearch.getText().toString(),
                            WishlistItemType.SERIE
                    ),
                    R.id.action_searchTmbdSerieFragment_to_wishlistItemFragment
            );
        }
    }

    public void populateListView(final List<BaseTvShow> tvShows) {
        ArrayAdapter<BaseTvShow> arrayAdapter;
        if (!toWishlist) {
            arrayAdapter = new TmdbTvResultsAdapter(
                    requireContext(),
                    (KinoApplication) requireActivity().getApplication(),
                    tvShows,
                    movieSearchResultClickCallback,
                    movieReviewCreationClickCallback);
        } else {
            arrayAdapter = new WishlistTvResultsAdapter(
                    requireContext(),
                    tvShows,
                    wishlistItemCallback);
        }

        binding.kinoResults.setAdapter(arrayAdapter);
        binding.kinoSearchProgressBar.setVisibility(View.GONE);
    }

}
