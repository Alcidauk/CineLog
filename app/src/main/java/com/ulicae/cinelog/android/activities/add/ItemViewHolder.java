package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ulicae.cinelog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

}
