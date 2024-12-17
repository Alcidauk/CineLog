package com.ulicae.cinelog.android.v2.fragments.review.add;

import static io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.services.AsyncDataTmdbService;
import com.ulicae.cinelog.databinding.TmdbItemRowBinding;
import com.ulicae.cinelog.network.DtoBuilderFromTmdbObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public abstract class TmdbResultAdapter<T> extends ArrayAdapter<T> {

    protected AsyncDataTmdbService<KinoDto> dataService;

    private DtoBuilderFromTmdbObject<T> builderFromTmdbObject;
    private TmdbItemRowBinding binding;

    protected final ReviewItemCallback reviewItemCallback;
    protected final ReviewCreationCallback reviewCreationCallback;

    private List<Disposable> disposables;

    public TmdbResultAdapter(Context context,
                             List<T> results,
                             AsyncDataTmdbService<KinoDto> dataService,
                             DtoBuilderFromTmdbObject<T> dtoBuilderFromTmdbObject,
                             ReviewItemCallback reviewItemCallback,
                             ReviewCreationCallback reviewCreationCallback) {
        super(context, R.layout.tmdb_item_row, results);
        this.dataService = dataService;
        this.builderFromTmdbObject = dtoBuilderFromTmdbObject;
        this.reviewItemCallback = reviewItemCallback;
        this.reviewCreationCallback = reviewCreationCallback;
        this.disposables = new ArrayList<>();
    }

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            binding = TmdbItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = TmdbItemRowBinding.bind(convertView);
        }

        final KinoDto kinoDto = builderFromTmdbObject.build(getItem(position));

        TmdbViewHolder holder = new TmdbViewHolder(binding);

        populateRatingBar(holder);
        populateTitle(kinoDto, holder);
        populateYear(kinoDto, holder);
        populatePoster(kinoDto, holder);

        final Long tmdbId = kinoDto.getTmdbKinoId();

        View finalConvertView = convertView;
        disposables.add(
                dataService
                        .getWithTmdbId(tmdbId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(mainThread())
                        // if single returns error, we admit it was not found in DB, so we use network result one
                        .onErrorReturnItem(kinoDto)
                        .subscribe((kino) -> {
                            if (kino.getId() == null) {
                                populateUnregistered(kino, holder);
                            } else {
                                populateReview(kino, holder);
                            }

                            finalConvertView.setOnClickListener(
                                    v -> viewDetails(
                                            kino,
                                            position,
                                            kino.getId() != null)
                            );
                        })
        );
        return convertView;
    }

    @Override
    public void clear() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    private void populateReview(KinoDto existingReview, TmdbViewHolder holder) {
        if (existingReview.getMaxRating() <= 5) {
            holder.getRatingBar().setRating(
                    existingReview.getRating() != null ? existingReview.getRating() : 0
            );

            holder.getRatingBarAsText().setVisibility(View.INVISIBLE);
            holder.getRatingBarMaxAsText().setVisibility(View.INVISIBLE);

            holder.getRatingBar().setVisibility(View.VISIBLE);
        } else {
            holder.getRatingBarAsText().setText(String.format("%s", existingReview.getRating()));
            holder.getRatingBarMaxAsText().setText(String.format("/%s", existingReview.getMaxRating()));

            holder.getRatingBarAsText().setVisibility(View.VISIBLE);
            holder.getRatingBarMaxAsText().setVisibility(View.VISIBLE);

            holder.getRatingBar().setVisibility(View.INVISIBLE);
        }

        holder.getAddButton().setVisibility(View.INVISIBLE);
        holder.getAddButton().setFocusable(false);
    }

    private void populateUnregistered(final KinoDto kinoDto, TmdbViewHolder holder) {
        holder.getRatingBar().setVisibility(View.INVISIBLE);
        holder.getRatingBarAsText().setVisibility(View.INVISIBLE);
        holder.getRatingBarMaxAsText().setVisibility(View.INVISIBLE);

        holder.getAddButton().setVisibility(View.VISIBLE);

        holder.getAddButton().setOnClickListener(view -> {
            addReview(kinoDto);
        });
    }

    protected void addReview(KinoDto kinoDto) {
        reviewCreationCallback.call(kinoDto);
    }

    protected void viewDetails(KinoDto kinoDto, int position, boolean inDb) {
        reviewItemCallback.call(kinoDto, position, inDb);
    }

    private void populatePoster(KinoDto kinoDto, TmdbViewHolder holder) {
        holder.getPoster().setLayoutParams(new RelativeLayout.LayoutParams(120, 150));
        if (kinoDto.getPosterPath() != null) {
            Glide.with(getContext())
                    .load("https://image.tmdb.org/t/p/w185" + kinoDto.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        } else {
            Glide.with(getContext())
                    .load(R.drawable.noimage_purple)
                    .centerCrop()
                    .crossFade()
                    .into(holder.getPoster());
        }
    }

    private void populateYear(KinoDto kinoDto, TmdbViewHolder holder) {
        if (kinoDto.getReleaseDate() != null && !kinoDto.getReleaseDate().equals("")) {
            holder.getYear().setText(String.format("%d", kinoDto.getYear()));
        } else {
            holder.getYear().setText("");
        }
    }

    private void populateTitle(KinoDto kinoDto, TmdbViewHolder holder) {
        holder.getTitle().setText(kinoDto.getTitle() != null ? kinoDto.getTitle() : "");
    }

    private void populateRatingBar(TmdbViewHolder holder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultMaxRateValue = prefs.getString("default_max_rate_value", "5");
        int maxRating = Integer.parseInt(defaultMaxRateValue);
        holder.getRatingBar().setNumStars(maxRating);
    }
}