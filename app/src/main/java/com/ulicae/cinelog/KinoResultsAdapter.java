package com.ulicae.cinelog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.dto.KinoDto;
import com.ulicae.cinelog.dto.KinoService;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.ulicae.cinelog.AddKino.RESULT_EDIT_REVIEW;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class KinoResultsAdapter extends ArrayAdapter<Movie> {

    private SimpleDateFormat sdf;

    private KinoService kinoService;

    public KinoResultsAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.search_result_item, movies);
        sdf = new SimpleDateFormat("yyyy");
        kinoService = new KinoService(((KinoApplication) ((AddKino) context).getApplication()).getDaoSession());
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO continue to clean that
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.kino_rating_bar_review);
        ImageButton addReviewButton = (ImageButton) convertView.findViewById(R.id.add_review_button);
        TextView title = (TextView) convertView.findViewById(R.id.kino_title);
        TextView yearTextView = (TextView) convertView.findViewById(R.id.kino_year);
        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.kino_poster);

        Movie movie = getItem(position);

        if (movie.title != null) {
            title.setText(movie.title);
        }

        String year = "";
        if (movie.release_date != null) {
            year = sdf.format(movie.release_date);
            yearTextView.setText(year);
        }

        if (movie.poster_path != null) {
            posterImageView.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + movie.poster_path)
                    .centerCrop()
                    .crossFade()
                    .into(posterImageView);
        } else {
            if (posterImageView != null)
                posterImageView.setImageResource(0);
        }

        final KinoDto kino = new KinoDto(
                null,
                movie.id.longValue(),
                movie.title,
                null,
                null,
                null,
                null,
                movie.poster_path,
                movie.overview,
                year != null && !"".equals(year) ? Integer.parseInt(year) : 0,
                year
        );

        final Integer m_id = movie.id;
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(m_id);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kino));
                    intent.putExtra("creation", true);
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) getContext()).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });

        KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(movie.id);
        if (kinoByTmdbMovieId != null) {
            ratingBar.setRating(kinoByTmdbMovieId.getRating());

            ratingBar.setVisibility(View.VISIBLE);
            addReviewButton.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setVisibility(View.INVISIBLE);
            addReviewButton.setVisibility(View.VISIBLE);
        }

        addReviewButton.setFocusable(false);

        return convertView;
    }
}
