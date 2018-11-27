package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.DataService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.DtoBuilderFromTmdbObject;

import java.util.List;

public abstract class ItemResultAdapter<T> extends ArrayAdapter<T> {

    protected DataService dataService;

    private DtoBuilderFromTmdbObject<T> builderFromTmdbObject;

    public ItemResultAdapter(Context context, List<T> results, DataService dataService, DtoBuilderFromTmdbObject<T> dtoBuilderFromTmdbObject) {
        super(context, R.layout.search_result_item, results);
        this.dataService = dataService;
        this.builderFromTmdbObject = dtoBuilderFromTmdbObject;
    }

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        final KinoDto kinoDto = builderFromTmdbObject.build(getItem(position));

        ItemViewHolder holder = new ItemViewHolder(convertView);

        populateRatingBar(holder);
        populateTitle(kinoDto, holder);
        populateYear(kinoDto, holder);
        populatePoster(kinoDto, holder);

        final Long tmdbId = kinoDto.getTmdbKinoId();
        populateAddButton(kinoDto, holder, tmdbId);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetails(kinoDto, position);
            }
        });

        KinoDto kinoByTmdbMovieId = dataService.getWithTmdbId(tmdbId);
        if (kinoByTmdbMovieId != null) {
            holder.getRatingBar().setRating(kinoByTmdbMovieId.getRating());

            holder.getRatingBar().setVisibility(View.VISIBLE);
            holder.getAddButton().setVisibility(View.INVISIBLE);
        } else {
            holder.getRatingBar().setVisibility(View.INVISIBLE);
            holder.getAddButton().setVisibility(View.VISIBLE);
        }

        holder.getAddButton().setFocusable(false);

        return convertView;
    }

    private void populateAddButton(final KinoDto kinoDto, ItemViewHolder holder, final Long tmdbId) {
        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReview(view, tmdbId, kinoDto);
            }
        });
    }

    protected abstract void addReview(View view, Long tmdbId, KinoDto kinoDto);

    protected abstract void viewDetails(KinoDto kinoDto, int position);

    private void populatePoster(KinoDto kinoDto, ItemViewHolder holder) {
        if (kinoDto.getPosterPath() != null) {
            holder.getPoster().setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + kinoDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        } else {
            if (holder.getPoster() != null)
                holder.getPoster().setImageResource(0);
        }
    }

    private void populateYear(KinoDto kinoDto, ItemViewHolder holder) {
        if (kinoDto.getReleaseDate() != null) {
            holder.getYear().setText(String.format("%d", kinoDto.getYear()));
        }
    }

    private void populateTitle(KinoDto kinoDto, ItemViewHolder holder) {
        holder.getTitle().setText(kinoDto.getTitle() != null ? kinoDto.getTitle() : "");
    }

    private void populateRatingBar(ItemViewHolder holder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
        int maxRating = Integer.parseInt(defaultMaxRateValue);
        holder.getRatingBar().setNumStars(maxRating);
    }
}