package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.services.reviews.KinoService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class MovieFragment extends ListFragment {

    @BindView(R.id.kino_list)
    ListView kino_list;

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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);
    }

    @Override
    protected List<KinoDto> getResults(int order) {
        if(order == -1) {
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

    private int getOrderFromPreferences() {
        return super.getOrderFromPreferences("default_movie_sort_type");
    }
}

