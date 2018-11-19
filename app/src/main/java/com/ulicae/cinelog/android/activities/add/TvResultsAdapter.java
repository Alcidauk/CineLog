package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.KinoService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.uwetrottmann.tmdb2.entities.TvShow;

import java.text.SimpleDateFormat;
import java.util.List;

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

    private SimpleDateFormat sdf;

    private KinoService kinoService;

    public TvResultsAdapter(Context context, List<TvShow> tvShows) {
        super(context, R.layout.search_result_item, tvShows);
        sdf = new SimpleDateFormat("yyyy");
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO continue to clean that
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.kino_rating_bar_review);
        //ImageButton addReviewButton = (ImageButton) convertView.findViewById(R.id.add_review_button);
        TextView title = (TextView) convertView.findViewById(R.id.kino_title);
        TextView yearTextView = (TextView) convertView.findViewById(R.id.kino_year);
        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.kino_poster);

        TvShow tvShow = getItem(position);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
        int maxRating = Integer.parseInt(defaultMaxRateValue);
        ratingBar.setNumStars(maxRating);

        if (tvShow.name != null) {
            title.setText(tvShow.name);
        }

        String year = "";
        if (tvShow.first_air_date != null) {
            year = sdf.format(tvShow.first_air_date);
            yearTextView.setText(year);
        }

        if (tvShow.poster_path != null) {
            posterImageView.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + tvShow.poster_path)
                    .centerCrop()
                    .crossFade()
                    .into(posterImageView);
        } else {
            if (posterImageView != null)
                posterImageView.setImageResource(0);
        }

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

        /*final Integer m_id = tvShow.id;
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(m_id);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kino));
                    intent.putExtra("creation", true);
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) getContext()).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });

        KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(tvShow.id);
        if (kinoByTmdbMovieId != null) {
            ratingBar.setRating(kinoByTmdbMovieId.getRating());

            ratingBar.setVisibility(View.VISIBLE);
            addReviewButton.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setVisibility(View.INVISIBLE);
            addReviewButton.setVisibility(View.VISIBLE);
        }

        addReviewButton.setFocusable(false);*/

        return convertView;
    }
}
