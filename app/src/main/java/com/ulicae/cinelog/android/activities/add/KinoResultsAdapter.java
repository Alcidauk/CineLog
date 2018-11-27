package com.ulicae.cinelog.android.activities.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.data.KinoService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.parceler.Parcels;

import java.util.List;

import static com.ulicae.cinelog.android.activities.add.AddKino.RESULT_EDIT_REVIEW;

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
public class KinoResultsAdapter extends ArrayAdapter<Movie> {


    private KinoService kinoService;

    private KinoBuilderFromMovie kinoBuilderFromMovie;

    public KinoResultsAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.search_result_item, movies);
        kinoService = new KinoService(((KinoApplication) ((AddKino) context).getApplication()).getDaoSession());
        kinoBuilderFromMovie = new KinoBuilderFromMovie();
    }

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        final KinoDto kinoDto = kinoBuilderFromMovie.build(getItem(position));

        ItemViewHolder holder = new ItemViewHolder(convertView);

        populateRatingBar(holder);
        populateTitle(kinoDto, holder);
        populateYear(kinoDto, holder);
        populatePoster(kinoDto, holder);

        final Long tmdbId = kinoDto.getTmdbKinoId();
        populateAddButton(kinoDto, holder, tmdbId);

        KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(tmdbId);
        if (kinoByTmdbMovieId != null) {
            holder.getRatingBar().setRating(kinoByTmdbMovieId.getRating());

            holder.getRatingBar().setVisibility(View.VISIBLE);
            holder.getAddButton().setVisibility(View.INVISIBLE);
        } else {
            holder.getRatingBar().setVisibility(View.INVISIBLE);
            holder.getAddButton().setVisibility(View.VISIBLE);
        }

        holder.getAddButton().setFocusable(false);

        return convertView;
    }

    private void populateAddButton(final KinoDto kinoDto, ItemViewHolder holder, final Long tmdbId) {
        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(tmdbId);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kinoDto));
                    intent.putExtra("creation", true);
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) getContext()).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });
    }

    private void populatePoster(KinoDto kinoDto, ItemViewHolder holder) {
        if (kinoDto.getPosterPath() != null) {
            holder.getPoster().setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + kinoDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        } else {
            if (holder.getPoster() != null)
                holder.getPoster().setImageResource(0);
        }
    }

    private void populateYear(KinoDto kinoDto, ItemViewHolder holder) {
        if (kinoDto.getReleaseDate() != null) {
            holder.getYear().setText(String.format("%d", kinoDto.getYear()));
        }
    }

    private void populateTitle(KinoDto kinoDto, ItemViewHolder holder) {
        holder.getTitle().setText(kinoDto.getTitle() != null ? kinoDto.getTitle() : "");
    }

    private void populateRatingBar(ItemViewHolder holder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
        int maxRating = Integer.parseInt(defaultMaxRateValue);
        holder.getRatingBar().setNumStars(maxRating);
    }
}
