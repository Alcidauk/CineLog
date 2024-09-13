package com.ulicae.cinelog.android.v2.fragments.review.room.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.databinding.FragmentSerieListBinding;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.services.ReviewAsyncService;

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
public class SerieReviewRoomListFragment extends ReviewRoomListFragment {

    private FragmentSerieListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        itemEntityType = ItemEntityType.SERIE;

        binding = FragmentSerieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    protected void createService() {
        service = new ReviewAsyncService((KinoApplication) getActivity().getApplication(), ItemEntityType.SERIE);
    }

    @Override
    protected void navigateToItem(KinoDto item, int position, boolean inDb, boolean fromSearch) {
        ((MainActivity) requireActivity()).navigateToItem(
                item, position, inDb, fromSearch
        );
    }

    @Override
    protected ListView getKinoList() {
        return binding != null ? binding.kinoList : null;
    }

    @Override
    protected void onFabClick() {
        ((MainActivity) requireActivity()).goToTmdbSerieSearch(false);
    }

    protected int getOrderFromPreferences() {
        return super.getOrderFromPreferences("default_movie_sort_type");
    }
}

