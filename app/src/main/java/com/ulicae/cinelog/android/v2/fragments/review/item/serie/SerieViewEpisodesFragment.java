package com.ulicae.cinelog.android.v2.fragments.review.item.serie;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.room.dto.SerieEpisodeDto;
import com.ulicae.cinelog.databinding.FragmentSerieViewEpisodesBinding;
import com.ulicae.cinelog.network.task.SerieEpisodesNetworkTask;
import com.ulicae.cinelog.room.dto.utils.to.TmdbSerieEpisodeToSerieEpisodeDtoBuilder;
import com.ulicae.cinelog.room.entities.ReviewWithEpisodes;
import com.ulicae.cinelog.room.entities.TmdbSerieEpisode;
import com.ulicae.cinelog.room.services.SerieEpisodeAsyncService;
import com.uwetrottmann.tmdb2.entities.TvEpisode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public class SerieViewEpisodesFragment extends Fragment {

    private List<Disposable> disposables;

    private FragmentSerieViewEpisodesBinding binding;

    private TvEpisodesAdapter tvEpisodesAdapter;

    private SerieEpisodeAsyncService serieEpisodeService;
    private Long tmdbId;
    private Long reviewId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        serieEpisodeService =
                new SerieEpisodeAsyncService(((KinoApplication) getActivity().getApplication()));
        disposables = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSerieViewEpisodesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        binding.serieViewEpisodesProgressBar.setVisibility(View.VISIBLE);
        this.tmdbId = requireArguments().getLong("tmdbId");
        this.reviewId = requireArguments().getLong("reviewId");

        if (this.tmdbId != null) {
            new SerieEpisodesNetworkTask(this).execute(tmdbId.intValue());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Disposable disposable : this.disposables) {
            disposable.dispose();
        }

        tvEpisodesAdapter.destroy();
    }

    public void populateEpisodeList(List<TvEpisode> tvEpisodes) {
        binding.serieViewEpisodesProgressBar.setVisibility(View.GONE);

        disposables.add(
                serieEpisodeService
                        .findSerieEpisodes(this.tmdbId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(mainThread())
                        .subscribe((reviewWithEpisodes) -> {
                            List<SerieEpisodeDto> episodeList = buildEpisodeList(tvEpisodes, reviewWithEpisodes);
                            createAndBindAdapter(episodeList);
                        }));
    }

    private @NonNull List<SerieEpisodeDto> buildEpisodeList(List<TvEpisode> tvEpisodes, ReviewWithEpisodes reviewWithEpisodes) {
        List<SerieEpisodeDto> allTvEpisodes =
                tvEpisodes.stream()
                        .map((tvEpisode) ->
                                new TmdbSerieEpisodeToSerieEpisodeDtoBuilder().build(tvEpisode, this.tmdbId, this.reviewId))
                        .collect(Collectors.toList());

        for (TmdbSerieEpisode watchedTvEpisode : reviewWithEpisodes.episodes) {
            SerieEpisodeDto watchedInList = allTvEpisodes
                    .stream()
                    .filter((episode) ->
                            episode.getTmdbEpisodeId().equals((int) watchedTvEpisode.getTmdbEpisodeId()))
                    .findAny()
                    .get();

            watchedInList.setWatchingDate(watchedTvEpisode.getWatchingDate());

            for(SerieEpisodeDto serieEpisodeDto : allTvEpisodes) {
                if(serieEpisodeDto.getTmdbEpisodeId().equals(watchedInList.getTmdbEpisodeId())){
                    serieEpisodeDto.setWatchingDate(watchedInList.getWatchingDate());
                }
            }
        }
        return allTvEpisodes;
    }

    private void createAndBindAdapter(List<SerieEpisodeDto> allTvEpisodes) {
        tvEpisodesAdapter =
                new TvEpisodesAdapter(
                        getContext(),
                        allTvEpisodes,
                        serieEpisodeService,
                        this.reviewId
                );

        if (binding != null) {
            binding.serieViewEpisodesList.setAdapter(tvEpisodesAdapter);
        }
    }
}
