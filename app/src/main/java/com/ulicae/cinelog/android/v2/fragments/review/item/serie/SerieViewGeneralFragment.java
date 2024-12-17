package com.ulicae.cinelog.android.v2.fragments.review.item.serie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.android.v2.fragments.review.room.item.ReviewItemDataFieldsInflater;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.databinding.FragmentSerieViewGeneralBinding;

import org.parceler.Parcels;

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
public class SerieViewGeneralFragment extends Fragment {

    private View view;
    private FragmentSerieViewGeneralBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSerieViewGeneralBinding.inflate(getLayoutInflater(), container, false);
        this.view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        SerieDto serieDto = Parcels.unwrap(requireArguments().getParcelable("kino"));
        new ReviewItemDataFieldsInflater(serieDto, getActivity(), binding.serieViewKinoContent, binding.serieReviewKinoContent).configureFields();
    }

}
