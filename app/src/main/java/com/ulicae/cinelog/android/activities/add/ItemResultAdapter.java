package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.DtoBuilderFromTmdbObject;

import java.util.List;

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
public abstract class ItemResultAdapter<T> extends ArrayAdapter<T> {

    protected DataService<? extends KinoDto> dataService;

    private DtoBuilderFromTmdbObject<T> builderFromTmdbObject;

    public ItemResultAdapter(Context context, List<T> results, DataService<? extends KinoDto> dataService, DtoBuilderFromTmdbObject<T> dtoBuilderFromTmdbObject) {
        super(context, R.layout.search_result_item, results);
        this.dataService = dataService;
        this.builderFromTmdbObject = dtoBuilderFromTmdbObject;
    }

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        final KinoDto kinoDto = builderFromTmdbObject.build(getItem(position));

        ItemViewHolder holder = new ItemViewHolder(convertView);

        populateRatingBar(holder);
        populateTitle(kinoDto, holder);
        populateYear(kinoDto, holder);
        populatePoster(kinoDto, holder);

        final Long tmdbId = kinoDto.getTmdbKinoId();
        populateAddButton(kinoDto, holder, tmdbId);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetails(kinoDto, position);
            }
        });

        KinoDto kinoByTmdbMovieId = dataService.getWithTmdbId(tmdbId);
        if (kinoByTmdbMovieId != null) {
            if(kinoByTmdbMovieId.getMaxRating() <= 5) {
                holder.getRatingBar().setRating(
                        kinoByTmdbMovieId.getRating() != null ? kinoByTmdbMovieId.getRating() : 0
                );

                holder.getRatingBarAsText().setVisibility(View.INVISIBLE);
                holder.getRatingBarMaxAsText().setVisibility(View.INVISIBLE);

                holder.getRatingBar().setVisibility(View.VISIBLE);
            } else {
                holder.getRatingBarAsText().setText(String.format("%s", kinoByTmdbMovieId.getRating()));
                holder.getRatingBarMaxAsText().setText(String.format("/%s", kinoByTmdbMovieId.getMaxRating()));

                holder.getRatingBarAsText().setVisibility(View.VISIBLE);
                holder.getRatingBarMaxAsText().setVisibility(View.VISIBLE);

                holder.getRatingBar().setVisibility(View.INVISIBLE);
            }

            holder.getAddButton().setVisibility(View.INVISIBLE);
        } else {
            holder.getRatingBar().setVisibility(View.INVISIBLE);
            holder.getRatingBarAsText().setVisibility(View.INVISIBLE);
            holder.getRatingBarMaxAsText().setVisibility(View.INVISIBLE);

            holder.getAddButton().setVisibility(View.VISIBLE);
        }

        holder.getAddButton().setFocusable(false);

        return convertView;
    }

    private void populateAddButton(final KinoDto kinoDto, ItemViewHolder holder, final Long tmdbId) {
        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReview(view, tmdbId, kinoDto);
            }
        });
    }

    protected abstract void addReview(View view, Long tmdbId, KinoDto kinoDto);

    protected abstract void viewDetails(KinoDto kinoDto, int position);

    private void populatePoster(KinoDto kinoDto, ItemViewHolder holder) {
        holder.getPoster().setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
        if (kinoDto.getPosterPath() != null) {
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + kinoDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        } else {
            Glide.with(getContext())
                    .load(R.drawable.noimage_purple)
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        }
    }

    private void populateYear(KinoDto kinoDto, ItemViewHolder holder) {
        if (kinoDto.getReleaseDate() != null && !kinoDto.getReleaseDate().equals("")) {
            holder.getYear().setText(String.format("%d", kinoDto.getYear()));
        } else {
            holder.getYear().setText("");
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