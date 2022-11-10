package com.ulicae.cinelog.android.v2.fragments.review.list;

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
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.databinding.FragmentMovieListBinding;

import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class MovieReviewListFragment extends ReviewListFragment {

    private FragmentMovieListBinding binding;

    @Override
    protected void createService() {
        service = new KinoService(((KinoApplication) getActivity().getApplication()).getDaoSession());
    }

    @Override
    protected String getDtoType() {
        return "kino";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        binding.fab.setOnClickListener(v -> ((MainActivity) requireActivity()).goToTmdbMovieSearch(false));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    protected List<KinoDto> getResults(int order) {
        if (order == -1) {
            order = getOrderFromPreferences();
        }
        switch (order) {
            case R.id.order_by_title_asc:
                return ((KinoService) service).getKinosByTitle(true);
            case R.id.order_by_title_desc:
                return ((KinoService) service).getKinosByTitle(false);
            case R.id.order_by_date_added_newest_first:
                return ((KinoService) service).getKinosByReviewDate(false);
            case R.id.order_by_date_added_oldest_first:
                return ((KinoService) service).getKinosByReviewDate(true);
            case R.id.order_by_rating_highest_first:
                return ((KinoService) service).getKinosByRating(false);
            case R.id.order_by_rating_lowest_first:
                return ((KinoService) service).getKinosByRating(true);
            case R.id.order_by_year_newest_first:
                return ((KinoService) service).getKinosByYear(false);
            case R.id.order_by_year_oldest_first:
                return ((KinoService) service).getKinosByYear(true);
            default:
                return ((KinoService) service).getAll();
        }
    }

    @Override
    protected ListView getKinoList() {
        return binding != null ? binding.kinoList : null;
    }

    private int getOrderFromPreferences() {
        return super.getOrderFromPreferences("default_movie_sort_type");
    }
}

