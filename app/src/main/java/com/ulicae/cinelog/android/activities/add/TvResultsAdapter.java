package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.android.activities.ViewUnregisteredKino;
import com.ulicae.cinelog.data.SerieService;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.ulicae.cinelog.network.SerieBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.TvShow;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.ulicae.cinelog.android.activities.add.AddSerieActivity.RESULT_VIEW_KINO;

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
public class TvResultsAdapter extends ArrayAdapter<TvShow> {

    private final SerieBuilderFromMovie serieBuilderFromMovie;

    private SimpleDateFormat sdf;

    private SerieService serieService;

    public TvResultsAdapter(Context context, List<TvShow> tvShows) {
        super(context, R.layout.search_result_item, tvShows);
        sdf = new SimpleDateFormat("yyyy");

        serieService = new SerieService(((KinoApplication) ((AddSerieActivity) context).getApplication()).getDaoSession());
        serieBuilderFromMovie = new SerieBuilderFromMovie();
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // TODO continue to clean that
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        final SerieDto serieDto = serieBuilderFromMovie.build(getItem(position));

        ItemViewHolder itemViewHolder = new ItemViewHolder(convertView);

        populateRatingBar(itemViewHolder);
        populateTitle(serieDto, itemViewHolder);
        populateYear(serieDto, itemViewHolder);
        populatePosterPath(serieDto, itemViewHolder);

        configureClick(position, convertView, serieDto);

        /*
                final KinoDto kino = new KinoDto(
                null,
                tvShow.id.longValue(),
                tvShow.name,
                null,
                null,
                null,
                null,
                tvShow.poster_path,
                null, // no desc ?
                year != null && !"".equals(year) ? Integer.parseInt(year) : 0,
                year
        );

        final Integer m_id = tvShow.id;
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = serieService.getKinoByTmdbMovieId(m_id);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kino));
                    intent.putExtra("creation", true);
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) getContext()).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });*/

        SerieDto serieByTmdbMovieId = serieService.getByTmdbMovieId(serieDto.getTmdbKinoId());
        if (serieByTmdbMovieId != null) {
            itemViewHolder.getRatingBar().setRating(serieByTmdbMovieId.getRating());

            itemViewHolder.getRatingBar().setVisibility(View.VISIBLE);
            itemViewHolder.getAddButton().setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.getRatingBar().setVisibility(View.INVISIBLE);
            itemViewHolder.getAddButton().setVisibility(View.VISIBLE);
        }

        itemViewHolder.getAddButton().setFocusable(false);

        return convertView;
    }

    private void configureClick(final int position, View convertView, final SerieDto serieDto) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerieDto kinoByTmdbMovieId = serieService.getByTmdbMovieId(serieDto.getTmdbKinoId());
                if (kinoByTmdbMovieId == null) {
                    Intent intent = new Intent(getContext(), ViewUnregisteredKino.class);
                    intent.putExtra("kino", Parcels.wrap(serieDto));

                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ViewKino.class);
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                    intent.putExtra("kino_position", position);
                    ((AppCompatActivity) getContext()).startActivityForResult(intent, RESULT_VIEW_KINO);
                }
            }
        });
    }

    private void populatePosterPath(SerieDto serieDto, ItemViewHolder itemViewHolder) {
        if (serieDto.getPosterPath() != null) {
            itemViewHolder.getPoster().setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + serieDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(itemViewHolder.getPoster());
        } else {
            if (itemViewHolder.getPoster() != null)
                itemViewHolder.getPoster().setImageResource(0);
        }
    }

    private void populateYear(SerieDto serieDto, ItemViewHolder itemViewHolder) {
        if (serieDto.getReleaseDate() != null) {
            itemViewHolder.getYear().setText(String.format("%d", serieDto.getYear()));
        }
    }

    private void populateTitle(SerieDto serieDto, ItemViewHolder itemViewHolder) {
        if (serieDto.getTitle() != null) {
            itemViewHolder.getTitle().setText(serieDto.getTitle());
        }
    }

    private void populateRatingBar(ItemViewHolder itemViewHolder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
        int maxRating = Integer.parseInt(defaultMaxRateValue);
        itemViewHolder.getRatingBar().setNumStars(maxRating);
    }
}
