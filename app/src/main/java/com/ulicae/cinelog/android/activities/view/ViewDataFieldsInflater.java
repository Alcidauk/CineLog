package com.ulicae.cinelog.android.activities.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Layout;
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
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.utils.image.ImageCacheDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewDataFieldsInflater {

    @BindView(R.id.view_kino_tmdb_image_layout)
    ImageView poster;
    @BindView(R.id.view_kino_tmdb_title)
    TextView title;
    @BindView(R.id.view_kino_tmdb_year)
    TextView year;
    @BindView(R.id.view_kino_tmdb_overview)
    TextView overview;
    @BindView(R.id.view_kino_review_rating)
    RatingBar rating;
    @BindView(R.id.view_kino_review_rating_as_text)
    TextView ratingAsText;
    @BindView(R.id.view_kino_review_review)
    TextView review;
    @BindView(R.id.view_kino_review_review_label)
    TextView reviewLabel;
    @BindView(R.id.view_kino_review_review_date)
    TextView review_date;

    @BindView(R.id.view_kino_tmdb_image_title_layout)
    LinearLayout view_kino_tmdb_image_title_layout;

    @BindView(R.id.view_kino_tmdb_overview_more_button)
    Button overview_more_button;

    @BindView(R.id.view_kino_review_tags_list)
    LinearLayout tagsList;

    private KinoDto kino;
    private Activity activity;

    public ViewDataFieldsInflater(KinoDto kino, Activity activity, View view) {
        this.kino = kino;
        this.activity = activity;

        ButterKnife.bind(this, view);
    }

    public void configureFields(){
        configureMaxRating();
        configureTitleAndPoster();
        configureReleaseDate();
        configureOverview();
        configureRating();
        configureReview();
        configureTags();
    }

    private void configureTags() {
        tagsList.removeAllViews();

        if(kino.getTags() != null) {
            for(TagDto tagDto : kino.getTags()) {
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
                new ViewGroup.LayoutParams(20,ViewGroup.LayoutParams.MATCH_PARENT)
        );

        TextView tagName = new TextView(activity);
        tagName.setText(tagDto.getName());
        tagName.setPadding(30,10,10,10);

        CardView cardView = new CardView(activity);
        cardView.setCardElevation(10);
        cardView.setRadius(10);
        cardView.setPadding(20,20,20,20);
        cardView.addView(tagColor);
        cardView.addView(tagName);

        RelativeLayout lay = new RelativeLayout(activity);
        lay.setPadding(20,20,20,20);
        lay.addView(cardView);

        return lay;
    }

    private void configureReview() {
        if(kino.getReview() == null || "".equals(kino.getReview())) {
            review.setVisibility(View.INVISIBLE);
            reviewLabel.setVisibility(View.INVISIBLE);
        } else {
            review.setVisibility(View.VISIBLE);
            reviewLabel.setVisibility(View.VISIBLE);

            review.setText(kino.getReview());
        }
        review_date.setText(getReviewDateAsString(kino.getReview_date()));
    }

    private void configureRating() {
        if (kino.getRating() != null) {
            rating.setRating(kino.getRating());
            ratingAsText.setText(String.format("%s", kino.getRating()));
        }
    }

    private void configureOverview() {
        overview.setText(kino.getOverview());
        if(kino.getOverview() == null || "".equals(kino.getOverview())){
            overview_more_button.setVisibility(View.INVISIBLE);
        }
    }

    private void configureReleaseDate() {
        String releaseDateLocal = kino.getReleaseDate();
        if(releaseDateLocal != null && !"".equals(releaseDateLocal)) {
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

    @OnClick(R.id.view_kino_tmdb_overview_more_button)
    void onToggleOverview() {
        if(poster.getVisibility() == View.VISIBLE){
            poster.setVisibility(View.GONE);
            overview_more_button.setText(R.string.view_kino_overview_see_less);

            overview.setEllipsize(null);
            overview.setMaxLines(Integer.MAX_VALUE);
            title.setEllipsize(null);
            title.setMaxLines(Integer.MAX_VALUE);

            ViewGroup.LayoutParams params = view_kino_tmdb_image_title_layout.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view_kino_tmdb_image_title_layout.setLayoutParams(params);
        } else {
            poster.setVisibility(View.VISIBLE);
            overview_more_button.setText(R.string.view_kino_overview_see_more);

            overview.setEllipsize(TextUtils.TruncateAt.END);
            overview.setMaxLines(4);
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setMaxLines(2);

            ViewGroup.LayoutParams params = view_kino_tmdb_image_title_layout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    200, activity.getResources().getDisplayMetrics());
            view_kino_tmdb_image_title_layout.setLayoutParams(params);
        }
    }
}
