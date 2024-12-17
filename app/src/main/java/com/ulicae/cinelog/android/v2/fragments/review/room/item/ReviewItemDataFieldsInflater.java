package com.ulicae.cinelog.android.v2.fragments.review.room.item;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.databinding.LayoutReviewItemKinoBinding;
import com.ulicae.cinelog.databinding.LayoutReviewItemReviewBinding;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class ReviewItemDataFieldsInflater {

    ImageView poster;
    TextView title;
    TextView year;
    TextView overview;
    RatingBar rating;
    TextView ratingAsText;
    TextView review;
    TextView reviewLabel;
    TextView reviewDate;

    LinearLayout tmdbImageTitle;
    Button overviewMoreButton;
    LinearLayout tagsList;

    private final KinoDto kino;
    private Activity activity;
    private final LayoutReviewItemKinoBinding viewKinoContentLayout;
    private final LayoutReviewItemReviewBinding reviewKinoContentLayout;

    public ReviewItemDataFieldsInflater(KinoDto kino,
                                        Activity activity,
                                        LayoutReviewItemKinoBinding viewKinoContentLayout,
                                        LayoutReviewItemReviewBinding reviewKinoContentLayout) {
        this.kino = kino;
        this.activity = activity;
        this.viewKinoContentLayout = viewKinoContentLayout;
        this.reviewKinoContentLayout = reviewKinoContentLayout;
    }

    public void configureFields() {
        bind();

        configureMaxRating();
        configureTitleAndPoster();
        configureReleaseDate();
        configureOverview();
        configureRating();
        configureReview();
        configureTags();
    }

    private void bind() {
        poster = viewKinoContentLayout.viewKinoTmdbImageLayout;
        title = viewKinoContentLayout.viewKinoTmdbTitle;
        year = viewKinoContentLayout.viewKinoTmdbYear;
        overview = viewKinoContentLayout.viewKinoTmdbOverview;
        tmdbImageTitle = viewKinoContentLayout.viewKinoTmdbImageTitleLayout;
        overviewMoreButton = viewKinoContentLayout.viewKinoTmdbOverviewMoreButton;

        rating = reviewKinoContentLayout.viewKinoReviewRating;
        ratingAsText = reviewKinoContentLayout.viewKinoReviewRatingAsText;
        review = reviewKinoContentLayout.viewKinoReviewReview;
        reviewLabel = reviewKinoContentLayout.viewKinoReviewReviewLabel;
        reviewDate = reviewKinoContentLayout.viewKinoReviewReviewDate;
        tagsList = reviewKinoContentLayout.viewKinoReviewTagsList;

        overviewMoreButton.setOnClickListener(this::onToggleOverview);
    }

    private void configureTags() {
        tagsList.removeAllViews();

        if (kino.getTags() != null) {
            for (TagDto tagDto : kino.getTags()) {
                RelativeLayout tagLayout = getLayoutForTag(tagDto);
                tagsList.addView(tagLayout);
            }
        }
    }

    @NonNull
    private RelativeLayout getLayoutForTag(TagDto tagDto) {
        View tagColor = new View(activity);
        tagColor.setBackgroundColor(Color.parseColor(tagDto.getColor()));
        tagColor.setVisibility(View.VISIBLE);
        tagColor.setLayoutParams(
                new ViewGroup.LayoutParams(20, ViewGroup.LayoutParams.MATCH_PARENT)
        );

        TextView tagName = new TextView(activity);
        tagName.setText(tagDto.getName());
        tagName.setPadding(30, 10, 10, 10);

        CardView cardView = new CardView(activity);
        cardView.setCardElevation(10);
        cardView.setRadius(10);
        cardView.setPadding(20, 20, 20, 20);
        cardView.addView(tagColor);
        cardView.addView(tagName);

        RelativeLayout lay = new RelativeLayout(activity);
        lay.setPadding(20, 20, 20, 20);
        lay.addView(cardView);

        return lay;
    }

    private void configureReview() {
        if (kino.getReview() == null || "".equals(kino.getReview())) {
            review.setVisibility(View.INVISIBLE);
            reviewLabel.setVisibility(View.INVISIBLE);
        } else {
            review.setVisibility(View.VISIBLE);
            reviewLabel.setVisibility(View.VISIBLE);

            review.setText(kino.getReview());
        }
        reviewDate.setText(getReviewDateAsString(kino.getReview_date()));
    }

    private void configureRating() {
        if (kino.getRating() != null) {
            rating.setRating(kino.getRating());
            ratingAsText.setText(String.format("%s", kino.getRating()));
        }
    }

    private void configureOverview() {
        overview.setText(kino.getOverview());
        if (kino.getOverview() == null || "".equals(kino.getOverview())) {
            overviewMoreButton.setVisibility(View.INVISIBLE);
        }
    }

    private void configureReleaseDate() {
        String releaseDateLocal = kino.getReleaseDate();
        if (releaseDateLocal != null && !"".equals(releaseDateLocal)) {
            SimpleDateFormat frenchSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            try {
                Date parsedDate = frenchSdf.parse(releaseDateLocal);
                String formattedDate = DateFormat.getDateFormat(activity.getApplicationContext()).format(parsedDate);
                year.setText(formattedDate);
            } catch (ParseException ignored) {
                year.setText(String.valueOf(kino.getYear()));
            }
        }
    }

    private void configureTitleAndPoster() {
        title.setText(kino.getTitle());
        if (kino.getPosterPath() != null && !"".equals(kino.getPosterPath())) {
            Glide.with(activity)
                    .load(new ImageCacheDownloader(activity.getCacheDir(), kino.getPosterPath())
                            .getPosterFinder().getImage(kino.getPosterPath()))
                    .centerCrop()
                    .crossFade()
                    .into(poster);
        }
    }

    private void configureMaxRating() {
        int maxRating;
        if (kino.getMaxRating() == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
            maxRating = Integer.parseInt(defaultMaxRateValue);
        } else {
            maxRating = kino.getMaxRating();
        }
        rating.setNumStars(maxRating);
        rating.setStepSize(0.5f);
    }

    private String getReviewDateAsString(Date review_date) {
        if (review_date != null) {
            return DateFormat.getDateFormat(activity.getApplicationContext()).format(review_date);
        }
        return null;
    }

    public void onToggleOverview(View view) {
        if (poster.getVisibility() == View.VISIBLE) {
            poster.setVisibility(View.GONE);
            overviewMoreButton.setText(R.string.view_kino_overview_see_less);

            overview.setEllipsize(null);
            overview.setMaxLines(Integer.MAX_VALUE);
            title.setEllipsize(null);
            title.setMaxLines(Integer.MAX_VALUE);

            ViewGroup.LayoutParams params = tmdbImageTitle.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tmdbImageTitle.setLayoutParams(params);
        } else {
            poster.setVisibility(View.VISIBLE);
            overviewMoreButton.setText(R.string.view_kino_overview_see_more);

            overview.setEllipsize(TextUtils.TruncateAt.END);
            overview.setMaxLines(4);
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setMaxLines(2);

            ViewGroup.LayoutParams params = tmdbImageTitle.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    200, activity.getResources().getDisplayMetrics());
            tmdbImageTitle.setLayoutParams(params);
        }
    }
}
