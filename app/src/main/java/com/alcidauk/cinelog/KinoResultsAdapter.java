package com.alcidauk.cinelog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.alcidauk.cinelog.dto.KinoDto;
import com.alcidauk.cinelog.dto.KinoService;
import com.bumptech.glide.Glide;
import com.uwetrottmann.tmdb2.entities.Movie;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alcidauk.cinelog.AddKino.RESULT_EDIT_REVIEW;

/**
 * Created by alcidauk on 15/02/18.
 */
public class KinoResultsAdapter extends BaseAdapter {

    private Context context;
    private List<Movie> movies;
    private SimpleDateFormat sdf;

    private Map<Integer, Boolean> resultsStatuses;

    private KinoService kinoService;

    public KinoResultsAdapter(Context context, List<Movie> movies) {
        this.context = context;
        resultsStatuses = new HashMap<>();
        if (movies != null) {
            this.movies = movies;

            sdf = new SimpleDateFormat("yyyy");

            kinoService = new KinoService(((KinoApplication) ((AddKino) context).getApplication()).getDaoSession());
        } else {
            this.movies = new ArrayList<>();
        }
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // createOrUpdate a new RelativeView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO clean that

        AddKino.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.search_result_item, null);
            holder = new AddKino.ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (AddKino.ViewHolder) convertView.getTag();
        }

        Movie movie = movies.get(position);

        // default the icons to disabled
        holder.rating_bar_review.setEnabled(false);
        holder.add_review_button.setVisibility(View.INVISIBLE);

        if (movie.title != null) {
            holder.title.setText(movie.title);
        }

        String year = "";
        if (movie.release_date != null) {
            year = sdf.format(movie.release_date);
            holder.year.setText(year);
        }

        if (movie.poster_path != null) {
            holder.poster.setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w185" + movie.poster_path)
                    .centerCrop()
                    .crossFade()
                    .into(holder.poster);
        } else {
            if (holder.poster != null)
                holder.poster.setImageResource(0);
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
                Integer.parseInt(year),
                year
        );

        final Integer m_id = movie.id;
        holder.add_review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditReview.class);

                KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(m_id);

                if (kinoByTmdbMovieId == null) {
                    intent.putExtra("kino", Parcels.wrap(kino));
                } else {
                    intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
                }

                ((Activity) context).startActivityForResult(intent, RESULT_EDIT_REVIEW);
            }
        });

        Boolean isReviewed = resultsStatuses.get(movie.id);
        if (isReviewed == null) {
            KinoDto kinoByTmdbMovieId = kinoService.getKinoByTmdbMovieId(movie.id);
            if (kinoByTmdbMovieId != null) {
                resultsStatuses.put(movie.id, true);
                isReviewed = true;
            } else {
                resultsStatuses.put(movie.id, false);
                isReviewed = false;
            }
        }

        if (isReviewed) {
            holder.rating_bar_review.setEnabled(true);
            holder.rating_bar_review.setRating(kinoService.getKinoByTmdbMovieId(movie.id).getRating());
        } else {
            holder.add_review_button.setVisibility(View.VISIBLE);
        }

        holder.add_review_button.setFocusable(false);

        return convertView;
    }
}
