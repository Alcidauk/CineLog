package com.ulicae.cinelog.android.v2.fragments.review.room.list;

import android.content.Context;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.databinding.ReviewListItemRowBinding;
import com.ulicae.cinelog.databinding.ReviewListYearRowBinding;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

import java.util.List;

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
public class ReviewListAdapter extends ArrayAdapter<Object> {

    public ReviewListAdapter(@NonNull Context context, @NonNull List<Object> objects) {
        super(context, R.layout.review_list_item_row, objects);
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object = getItem(position);
        boolean isObjectString = object instanceof String;

        ViewBinding binding;

        if (needInflate(convertView, object)) {
            binding = isObjectString ?
                    ReviewListYearRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false) :
                    ReviewListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        } else {
            binding = isObjectString ? ReviewListYearRowBinding.bind(convertView) : ReviewListItemRowBinding.bind(convertView);
        }

        if (!isObjectString) {
            initKinoListView(binding, (KinoDto) object);
        } else {
            initYearView(binding, (String) object);
        }

        convertView = binding.getRoot();
        return convertView;
    }

    private boolean needInflate(View convertView, Object item) {
        return convertView == null
                || (item instanceof String && convertView.getId() == R.id.main_result_item)
                || (item instanceof KinoDto && convertView.getId() == R.id.header_result_item);
    }

    private void initKinoListView(ViewBinding viewBinding, KinoDto movie) {
        ReviewListViewHolder holder = new ReviewListViewHolder(viewBinding);

        if (movie != null) {
            LinearLayout tagLayout = holder.getKinoTags();
            tagLayout.removeAllViews();
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

            initRating(holder, movie);
        }
    }

    private void initYearView(ViewBinding viewBinding, String object) {
        ReviewListYearViewHolder holder = new ReviewListYearViewHolder((ReviewListYearRowBinding) viewBinding);
        holder.getKinoTitle().setText(object);
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

    private void initRating(ReviewListViewHolder holder, KinoDto movie) {
        RatingBar kinoRatingRatingBar = holder.getKinoRatingBar();
        TextView kinoRatingRatingBarAsText = holder.getKinoRatingBarAsText();
        TextView kinoRatingRatingBarMaxAsText = holder.getKinoRatingBarMaxAsText();

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
