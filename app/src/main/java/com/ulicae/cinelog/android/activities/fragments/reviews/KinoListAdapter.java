package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

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
        boolean isObjectString = object instanceof String;

        if(needInflate(convertView, object)) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    isObjectString ? R.layout.header_result_item : R.layout.main_result_item,
                    parent,
                    false
            );
        }

        if(!isObjectString) {
            return getKinoListView(convertView, (KinoDto) object);
        }
        return getYearView(convertView, parent, (String) object);
    }

    private boolean needInflate(View convertView, Object item) {
        return convertView == null
                || (item instanceof String && convertView.getId() == R.id.main_result_item)
                || (item instanceof KinoDto && convertView.getId() == R.id.header_result_item);
    }

    private View getKinoListView(View convertView, KinoDto movie) {
        KinoListViewHolder holder = new KinoListViewHolder(convertView);

        if (movie != null) {
            LinearLayout tagLayout = holder.getKinoTags();
            for (TagDto tagDto : movie.getTags()) {
                GradientDrawable gd = getTagDot(tagDto);

                LinearLayout shapeLayout = new LinearLayout(getContext());
                shapeLayout.setBackground(gd);

                tagLayout.addView(shapeLayout);
            }

            holder.getKinoTitle().setText(movie.getTitle());

            TextView kinoYearTextView = holder.getKinoYear();
            if (movie.getYear() != 0) {
                kinoYearTextView.setText(String.valueOf(movie.getYear()));
            } else {
                kinoYearTextView.setText("");
            }

            ImageView kinoPosterImageView = holder.getKinoPoster();
            if (movie.getPosterPath() != null && !"".equals(movie.getPosterPath())) {
                Glide.with(getContext())
                        .load(
                                new ImageCacheDownloader(
                                        getContext().getExternalCacheDir(),
                                        movie.getPosterPath())
                                        .getPosterFinder()
                                        .getImage(movie.getPosterPath()))
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

            TextView kinoReviewDate = holder.getKinoReviewDate();
            ImageView kinoReviewDateLogo = holder.getKinoReviewDateLogo();
            if (movie.getReview_date() != null) {
                kinoReviewDate.setText(DateFormat.getDateFormat(getContext()).format(movie.getReview_date()));
                kinoReviewDateLogo.setVisibility(View.VISIBLE);
            } else {
                kinoReviewDate.setText("");
                kinoReviewDateLogo.setVisibility(View.INVISIBLE);
            }

            initRating(convertView, holder.getKinoRatingBar(), movie);
        }

        return convertView;
    }

    @NonNull
    private View getYearView(View convertView, ViewGroup parent, String object) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_result_item, parent, false);
        }

        KinoListYearViewHolder holder = new KinoListYearViewHolder(convertView);
        holder.getKinoTitle().setText(object);

        return convertView;
    }

    @NonNull
    private GradientDrawable getTagDot(TagDto tagDto) {
        GradientDrawable gd = new GradientDrawable();
        gd.setSize(30, 30);
        gd.setCornerRadius(15);
        // like a padding
        gd.setStroke(5, Color.parseColor("#00000000"));
        gd.setColor(Color.parseColor(tagDto.getColor()));

        return gd;
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
