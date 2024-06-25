package com.ulicae.cinelog.android.v2.fragments.wishlist.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.reviews.room.WishlistService;
import com.ulicae.cinelog.databinding.FragmentMovieListBinding;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ItemEntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class MovieWishlistRoomFragment extends WishlistFragment {

    private FragmentMovieListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        AppDatabase appDb = ((KinoApplication) getActivity().getApplication()).getDb();

        actionToItem = R.id.action_nav_wishlist_room_movie_to_wishlistItemFragment;
        service = new WishlistService(appDb, ItemEntityType.MOVIE);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void onFabClick() {
        ((MainActivity) requireActivity()).goToTmdbMovieSearch(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_simple, menu);
    }

    @Override
    protected ListView getKinoList() {
        return binding != null ? binding.kinoList : null;
    }

    protected List<WishlistDataDto> getResults(int order) {
        List<WishlistDataDto> fetchedDtos;
        switch (order) {
            default:
                fetchedDtos = service.getAll();
                break;
        }

        return new ArrayList<>(fetchedDtos);
    }

}
