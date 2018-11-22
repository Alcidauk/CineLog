package com.ulicae.cinelog.android.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.SerieService;
import com.ulicae.cinelog.data.dto.KinoDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class SerieFragment extends ListFragment {

    @BindView(R.id.kino_list)
    ListView kino_list;

    public SerieFragment() {
    }

    @Override
    protected void createService() {
        service = new SerieService(((KinoApplication) getActivity().getApplication()).getDaoSession());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected List<KinoDto> getResults(int order) {
        switch (order) {
           /* case 1:
                return ((KinoService) service).getKinosByReviewDate(false);
            case 2:
                return ((KinoService) service).getKinosByReviewDate(true);
            case 3:
                return ((KinoService) service).getKinosByRating(false);
            case 4:
                return ((KinoService) service).getKinosByRating(true);
            case 5:
                return ((KinoService) service).getKinosByYear(false);
            case 6:
                return ((KinoService) service).getKinosByYear(true);*/
           // default:
            //    return ((SerieService) service).getAll();
        }

        return new ArrayList<>();
    }

}
