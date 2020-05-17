package com.ulicae.cinelog.android.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ulicae.cinelog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class TvEpisodeViewHolder {

    @BindView(R.id.serie_episode_item_name)
    TextView episodeName;
    @BindView(R.id.serie_episode_item_date)
    TextView episodeDate;
    @BindView(R.id.serie_episode_item_season_number)
    TextView seasonNumber;
    @BindView(R.id.serie_episode_item_episode_number)
    TextView episodeNumber;

    @BindView(R.id.serie_episode_item_watched)
    ImageView episodeWatched;
    @BindView(R.id.serie_episode_item_watching_date)
    TextView episodeWatchingDate;


    TvEpisodeViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getEpisodeName() {
        return episodeName;
    }

    public TextView getEpisodeDate() {
        return episodeDate;
    }

    public TextView getSeasonNumber() {
        return seasonNumber;
    }

    public TextView getEpisodeNumber() {
        return episodeNumber;
    }

    public ImageView getEpisodeWatched() {
        return episodeWatched;
    }

    public TextView getEpisodeWatchingDate() {
        return episodeWatchingDate;
    }
}
