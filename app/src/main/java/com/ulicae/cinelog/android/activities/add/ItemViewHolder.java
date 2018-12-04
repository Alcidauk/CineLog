package com.ulicae.cinelog.android.activities.add;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ulicae.cinelog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
public class ItemViewHolder {

    @BindView(R.id.kino_title)
    TextView title;
    @BindView(R.id.kino_year)
    TextView year;
    @BindView(R.id.kino_poster)
    ImageView poster;

    @BindView(R.id.add_review_button)
    ImageButton add_review_button;

    @BindView(R.id.kino_rating_bar_review)
    RatingBar kino_rating_bar_review;

    @BindView(R.id.search_result_item_rating_bar_as_text)
    TextView search_result_item_rating_bar_as_text;
    @BindView(R.id.search_result_item_rating_bar_max_as_text)
    TextView search_result_item_rating_bar_max_as_text;

    ItemViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getYear() {
        return year;
    }

    public ImageView getPoster() {
        return poster;
    }

    public ImageButton getAddButton() {
        return add_review_button;
    }

    public RatingBar getRatingBar() {
        return kino_rating_bar_review;
    }

    public TextView getRatingBarAsText() {
        return search_result_item_rating_bar_as_text;
    }

    public TextView getRatingBarMaxAsText() {
        return search_result_item_rating_bar_max_as_text;
    }
}
