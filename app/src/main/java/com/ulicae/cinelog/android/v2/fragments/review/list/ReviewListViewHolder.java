package com.ulicae.cinelog.android.v2.fragments.review.list;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import com.ulicae.cinelog.databinding.HeaderResultItemBinding;
import com.ulicae.cinelog.databinding.MainResultItemBinding;

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
        return binding instanceof HeaderResultItemBinding ?
                ((HeaderResultItemBinding) binding).mainResultKinoTitle :
                getMainResultItemBinding().mainResultKinoTitle;
    }

    public TextView getKinoYear() {
        return getMainResultItemBinding().mainResultKinoYear;
    }

    public ImageView getKinoPoster() {
        return getMainResultItemBinding().mainResultKinoPoster;
    }

    public ImageView getKinoReviewDateLogo() {
        return getMainResultItemBinding().mainResultKinoReviewDateLogo;
    }

    public RatingBar getKinoRatingBar() {
        return getMainResultItemBinding().mainResultKinoRatingBarSmall;
    }

    public TextView getKinoRatingBarAsText() {
        return getMainResultItemBinding().mainResultKinoRatingBarAsText;
    }

    public TextView getKinoRatingBarMaxAsText() {
        return getMainResultItemBinding().mainResultKinoRatingBarMaxAsText;
    }

    public TextView getKinoReviewDate() {
        return getMainResultItemBinding().mainResultKinoReviewDate;
    }

    public LinearLayout getKinoTags() {
        return getMainResultItemBinding().mainResultKinoTags;
    }

    private MainResultItemBinding getMainResultItemBinding() {
        if (binding instanceof MainResultItemBinding) {
            return ((MainResultItemBinding) binding);
        }
        throw new ClassCastException();
    }
}
