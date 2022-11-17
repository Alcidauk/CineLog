package com.ulicae.cinelog.android.v2.fragments.review.item.serie;

import android.widget.ImageView;
import android.widget.TextView;

import com.ulicae.cinelog.databinding.SerieEpisodeResultItemBinding;

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
class TvEpisodeViewHolder {

    private final SerieEpisodeResultItemBinding binding;

    TvEpisodeViewHolder(SerieEpisodeResultItemBinding binding) {
        this.binding = binding;
    }

    public TextView getEpisodeName() {
        return binding.serieEpisodeItemName;
    }

    public TextView getEpisodeDate() {
        return binding.serieEpisodeItemDate;
    }

    public TextView getSeasonNumber() {
        return binding.serieEpisodeItemSeasonNumber;
    }

    public TextView getEpisodeNumber() {
        return binding.serieEpisodeItemEpisodeNumber;
    }

    public ImageView getEpisodeWatched() {
        return binding.serieEpisodeItemWatched;
    }

    public TextView getEpisodeWatchingDate() {
        return binding.serieEpisodeItemWatchingDate;
    }
}
