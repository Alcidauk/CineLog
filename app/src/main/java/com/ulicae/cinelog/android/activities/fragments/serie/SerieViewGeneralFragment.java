package com.ulicae.cinelog.android.activities.fragments.serie;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.view.ViewDataFieldsInflater;
import com.ulicae.cinelog.data.dto.SerieDto;

import org.parceler.Parcels;

import butterknife.ButterKnife;

/**
 * CineLog Copyright 2020 Pierre Rognon
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
public class SerieViewGeneralFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie_view_general, container, false);
        ButterKnife.bind(this, view);

        SerieDto serieDto = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("kino"));
        new ViewDataFieldsInflater(serieDto, getActivity(), view).configureFields();

        this.view = view;
        return view;
    }

    public void updateKino(SerieDto updatedSerie) {
        new ViewDataFieldsInflater(updatedSerie, getActivity(), view).configureFields();
    }
}
