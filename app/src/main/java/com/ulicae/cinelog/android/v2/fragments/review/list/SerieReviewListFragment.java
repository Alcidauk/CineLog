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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.v2.activities.MainActivity;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.databinding.FragmentSerieListBinding;

import java.util.ArrayList;
import java.util.List;

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
public class SerieReviewListFragment extends ReviewListFragment {

    private FragmentSerieListBinding binding;

    public SerieReviewListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSerieListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_serie, menu);
    }

    @Override
    protected void createService() {
        service = new SerieService(((KinoApplication) getActivity().getApplication()).getDaoSession(), getContext());
    }

    @Override
    protected List<KinoDto> getResults(int order) {
        if (order == -1) {
            order = getOrderFromPreferences();
        }

        List<SerieDto> fetchedDtos;
        switch (order) {
            case R.id.order_by_title_asc:
                fetchedDtos = ((SerieService) service).getAllByTitle(true);
                break;
            case R.id.order_by_title_desc:
                fetchedDtos = ((SerieService) service).getAllByTitle(false);
                break;
            /*case R.id.order_by_date_added_newest_first:
                return ((KinoService) service).getKinosByReviewDate(false);
            case R.id.order_by_date_added_oldest_first:
                return ((KinoService) service).getKinosByReviewDate(true);*/
            case R.id.order_by_rating_highest_first:
                fetchedDtos = ((SerieService) service).getAllByRating(false);
                break;
            case R.id.order_by_rating_lowest_first:
                fetchedDtos = ((SerieService) service).getAllByRating(true);
                break;
            /*case R.id.order_by_year_newest_first:
                return ((KinoService) service).getKinosByYear(false);
            case R.id.order_by_year_oldest_first:
                return ((KinoService) service).getKinosByYear(true);*/
            default:
                fetchedDtos = ((SerieService) service).getAll();
                break;
        }

        return new ArrayList<KinoDto>(fetchedDtos);
    }

    @Override
    protected ListView getKinoList() {
        return binding != null ? binding.kinoList : null;
    }

    @Override
    protected void onFabClick() {
        ((MainActivity) requireActivity()).goToTmdbSerieSearch(false);
    }

    private int getOrderFromPreferences() {
        return super.getOrderFromPreferences("default_serie_sort_type");
    }

}
