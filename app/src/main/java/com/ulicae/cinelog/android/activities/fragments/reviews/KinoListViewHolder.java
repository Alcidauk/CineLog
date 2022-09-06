package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ulicae.cinelog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
public class KinoListViewHolder {

    @BindView(R.id.main_result_kino_title)
    TextView kinoTitle;
    @Nullable
    @BindView(R.id.main_result_kino_year)
    TextView kinoYear;

    @Nullable
    @BindView(R.id.main_result_kino_poster)
    ImageView kinoPoster;
    @Nullable
    @BindView(R.id.main_result_kino_review_date_logo)
    ImageView kinoReviewDateLogo;

    @Nullable
    @BindView(R.id.main_result_kino_rating_bar_small)
    RatingBar kinoRatingBar;

    @Nullable
    @BindView(R.id.main_result_kino_review_date)
    TextView kinoReviewDate;

    @Nullable
    @BindView(R.id.main_result_kino_tags)
    LinearLayout kinoTags;

    KinoListViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getKinoTitle() {
        return kinoTitle;
    }

    public TextView getKinoYear() {
        return kinoYear;
    }

    public ImageView getKinoPoster() {
        return kinoPoster;
    }

    public ImageView getKinoReviewDateLogo() {
        return kinoReviewDateLogo;
    }

    public RatingBar getKinoRatingBar() {
        return kinoRatingBar;
    }

    public TextView getKinoReviewDate() {
        return kinoReviewDate;
    }

    public LinearLayout getKinoTags() {
        return kinoTags;
    }
}
