package com.ulicae.cinelog.android.v2.fragments.review.list;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import com.ulicae.cinelog.databinding.ReviewListYearRowBinding;
import com.ulicae.cinelog.databinding.ReviewListItemRowBinding;

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
public class ReviewListViewHolder {

    private final ViewBinding binding;

    ReviewListViewHolder(ViewBinding binding) {
        this.binding = binding;
    }

    public TextView getKinoTitle() {
        return binding instanceof ReviewListYearRowBinding ?
                ((ReviewListYearRowBinding) binding).mainResultKinoTitle :
                getReviewListItemRowBinding().mainResultKinoTitle;
    }

    public TextView getKinoYear() {
        return getReviewListItemRowBinding().mainResultKinoYear;
    }

    public ImageView getKinoPoster() {
        return getReviewListItemRowBinding().mainResultKinoPoster;
    }

    public ImageView getKinoReviewDateLogo() {
        return getReviewListItemRowBinding().mainResultKinoReviewDateLogo;
    }

    public RatingBar getKinoRatingBar() {
        return getReviewListItemRowBinding().mainResultKinoRatingBarSmall;
    }

    public TextView getKinoRatingBarAsText() {
        return getReviewListItemRowBinding().mainResultKinoRatingBarAsText;
    }

    public TextView getKinoRatingBarMaxAsText() {
        return getReviewListItemRowBinding().mainResultKinoRatingBarMaxAsText;
    }

    public TextView getKinoReviewDate() {
        return getReviewListItemRowBinding().mainResultKinoReviewDate;
    }

    public LinearLayout getKinoTags() {
        return getReviewListItemRowBinding().mainResultKinoTags;
    }

    private ReviewListItemRowBinding getReviewListItemRowBinding() {
        if (binding instanceof ReviewListItemRowBinding) {
            return ((ReviewListItemRowBinding) binding);
        }
        throw new ClassCastException();
    }
}
