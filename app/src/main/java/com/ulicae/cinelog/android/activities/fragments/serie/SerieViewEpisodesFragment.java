package com.ulicae.cinelog.android.activities.fragments.serie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.android.activities.TvEpisodesAdapter;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieEpisodeDto;
import com.ulicae.cinelog.data.services.reviews.SerieEpisodeService;
import com.ulicae.cinelog.databinding.FragmentSerieViewEpisodesBinding;
import com.ulicae.cinelog.network.task.SerieEpisodesNetworkTask;
import com.uwetrottmann.tmdb2.entities.TvEpisode;

import org.parceler.Parcels;

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
public class SerieViewEpisodesFragment extends Fragment {

    private FragmentSerieViewEpisodesBinding binding;

    private SerieEpisodeService serieEpisodeService;
    private SerieDto serieDto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        serieEpisodeService = new SerieEpisodeService(
                ((KinoApplication) getActivity().getApplication()).getDaoSession()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSerieViewEpisodesBinding.inflate(inflater, container, false);

        binding.serieViewEpisodesProgressBar.setVisibility(View.VISIBLE);
        this.serieDto = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("kino"));

        new SerieEpisodesNetworkTask(this).execute(this.serieDto.getTmdbKinoId().intValue());

        return binding.getRoot();
    }

    public void populateEpisodeList(List<TvEpisode> tvEpisodes) {
        binding.serieViewEpisodesProgressBar.setVisibility(View.GONE);

        List<SerieEpisodeDto> dtoEpisodes = serieEpisodeService.getDtoEpisodes(tvEpisodes,
                this.serieDto.getTmdbKinoId());

        ArrayAdapter<SerieEpisodeDto> arrayAdapter = new TvEpisodesAdapter(
                getContext(), dtoEpisodes, serieEpisodeService);

        if (binding != null) {
            binding.serieViewEpisodesList.setAdapter(arrayAdapter);
        }
    }


}
