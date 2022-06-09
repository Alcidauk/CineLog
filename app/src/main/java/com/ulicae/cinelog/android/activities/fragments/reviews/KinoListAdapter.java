package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

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
class KinoListAdapter extends ArrayAdapter<Object> {

    public KinoListAdapter(@NonNull Context context, @NonNull List<Object> objects) {
        super(context, R.layout.main_result_item, objects);
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object = getItem(position);
        if (object instanceof String) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_result_item, parent, false);

            TextView viewById = (TextView) convertView.findViewById(R.id.main_result_kino_title);
            viewById.setText((String) object);

            return convertView;
        }

        // don't make it conditional => since we can have strings, we must inflate for all items.
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_result_item, parent, false);


        TextView kinoTitleTextView = (TextView) convertView.findViewById(R.id.main_result_kino_title);
        TextView kinoYearTextView = (TextView) convertView.findViewById(R.id.main_result_kino_year);
        ImageView kinoPosterImageView = (ImageView) convertView.findViewById(R.id.main_result_kino_poster);
        RatingBar kinoRatingRatingBar = (RatingBar) convertView.findViewById(R.id.main_result_kino_rating_bar_small);
        TextView kinoReviewDate = (TextView) convertView.findViewById(R.id.main_result_kino_review_date);
        ImageView kinoReviewDateLogo = (ImageView) convertView.findViewById(R.id.main_result_kino_review_date_logo);
        KinoDto movie = (KinoDto) object;

        if (movie != null) {
            kinoTitleTextView.setText(movie.getTitle());

            if (movie.getYear() != 0) {
                kinoYearTextView.setText(String.valueOf(movie.getYear()));
            } else {
                kinoYearTextView.setText("");
            }

            if (movie.getPosterPath() != null && !"".equals(movie.getPosterPath())) {
                Glide.with(getContext())
                        .load(new ImageCacheDownloader(movie.getPosterPath())
                                .getPosterFinder().getImage(movie.getPosterPath()))
                        .centerCrop()
                        .crossFade()
                        .into(kinoPosterImageView);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.noimage_purple)
                        .centerCrop()
                        .crossFade()
                        .into(kinoPosterImageView);
            }

            if (movie.getReview_date() != null) {
                kinoReviewDate.setText(DateFormat.getDateFormat(getContext()).format(movie.getReview_date()));
                kinoReviewDateLogo.setVisibility(View.VISIBLE);
            } else {
                kinoReviewDate.setText("");
                kinoReviewDateLogo.setVisibility(View.INVISIBLE);
            }

            initRating(convertView, kinoRatingRatingBar, movie);
        }

        return convertView;
    }

    private void initRating(View convertView, RatingBar kinoRatingRatingBar, KinoDto movie) {
        TextView kinoRatingRatingBarAsText = (TextView) convertView.findViewById(R.id.main_result_kino_rating_bar_as_text);
        TextView kinoRatingRatingBarMaxAsText = (TextView) convertView.findViewById(R.id.main_result_kino_rating_bar_max_as_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        int maxRating;
        if (movie.getMaxRating() == null) {
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = movie.getMaxRating();
        }

        if (maxRating <= 5) {
            kinoRatingRatingBarAsText.setVisibility(View.INVISIBLE);
            kinoRatingRatingBarMaxAsText.setVisibility(View.INVISIBLE);
            kinoRatingRatingBar.setVisibility(View.VISIBLE);

            kinoRatingRatingBar.setStepSize(0.5f);
            kinoRatingRatingBar.setNumStars(maxRating);

            if (movie.getRating() != null) {
                kinoRatingRatingBar.setRating(movie.getRating());
            } else {
                kinoRatingRatingBar.setRating(0);
            }
        } else {
            kinoRatingRatingBar.setVisibility(View.INVISIBLE);
            kinoRatingRatingBarAsText.setVisibility(View.VISIBLE);
            kinoRatingRatingBarMaxAsText.setVisibility(View.VISIBLE);

            kinoRatingRatingBarAsText.setText(String.format("%s", movie.getRating()));
            kinoRatingRatingBarMaxAsText.setText(String.format("/%s", maxRating));
        }
    }

}
