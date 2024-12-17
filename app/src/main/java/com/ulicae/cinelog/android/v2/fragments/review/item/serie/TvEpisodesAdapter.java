package com.ulicae.cinelog.android.v2.fragments.review.item.serie;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.SerieEpisodeDto;
import com.ulicae.cinelog.databinding.SerieEpisodeResultItemBinding;
import com.ulicae.cinelog.room.services.SerieEpisodeAsyncService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
class TvEpisodesAdapter extends ArrayAdapter<SerieEpisodeDto> {

    private SerieEpisodeAsyncService serieEpisodeService;
    private final Long reviewId;
    private SerieEpisodeResultItemBinding binding;

    private List<Disposable> disposables;

    TvEpisodesAdapter(Context context, List<SerieEpisodeDto> results, SerieEpisodeAsyncService serieEpisodeService, Long reviewId) {
        super(context, R.layout.serie_episode_result_item, results);
        this.serieEpisodeService = serieEpisodeService;
        this.reviewId = reviewId;
        this.disposables = new ArrayList<>();
    }

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            binding = SerieEpisodeResultItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
        } else {
            binding = SerieEpisodeResultItemBinding.bind(convertView);
        }

        SerieEpisodeDto item = getItem(position);

        TvEpisodeViewHolder holder = new TvEpisodeViewHolder(binding);

        if (item != null) {
            setDataInView(item, holder);
            setWatchingDataInView(item, holder);
            convertView.setOnClickListener(v -> registerWatching(item));
            convertView.setOnLongClickListener(v -> unregisterWatching(item));
        }

        return convertView;
    }

    private void setDataInView(SerieEpisodeDto item, TvEpisodeViewHolder holder) {
        holder.getEpisodeName().setText(item.getName());
        holder.getEpisodeDate().setText(
                item.getAirDate() != null ?
                        DateFormat.getDateFormat(getContext()).format(item.getAirDate()) : ""
        );
        holder.getSeasonNumber().setText(
                item.getSeasonNumber() != null ? String.valueOf(item.getSeasonNumber() + 1) : ""
        );
        holder.getEpisodeNumber().setText(
                item.getEpisodeNumber() != null ? String.valueOf(item.getEpisodeNumber()) : ""
        );

        holder.getEpisodeWatchingDate().setText(
                item.getWatchingDate() != null ?
                        DateFormat.getDateFormat(getContext()).format(item.getWatchingDate()) : ""
        );

        // The watching date is the discriminant for a watched episode
        if (item.getWatchingDate() != null) {
            holder.getEpisodeWatched().setImageResource(R.drawable.round_eye_purple);
        } else {
            holder.getEpisodeWatched().setImageResource(R.drawable.round_eye_grey);
        }
    }

    private void setWatchingDataInView(SerieEpisodeDto item, TvEpisodeViewHolder holder) {
        holder.getEpisodeWatchingDate().setText(
                item.getWatchingDate() != null ?
                        DateFormat.getDateFormat(getContext()).format(item.getWatchingDate()) : ""
        );

        if (item.getWatchingDate() != null) {
            holder.getEpisodeWatched().setImageResource(R.drawable.round_eye_purple);
        } else {
            holder.getEpisodeWatched().setImageResource(R.drawable.round_eye_grey);
        }
    }

    private void registerWatching(SerieEpisodeDto item) {
        if (item.getWatchingDate() != null) {
            Toast.makeText(getContext(), getContext().getString(R.string.serie_episode_delete_hint), Toast.LENGTH_LONG).show();
        } else {
            // We need the review id because of the foreign key
            item.setReviewId(reviewId);

            disposables.add(
                    serieEpisodeService
                            .createOrUpdate(item)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((newItemId) -> findAndUpdateItem(item, newItemId))
            );
        }
    }

    private void findAndUpdateItem(SerieEpisodeDto item, Long newItemId) {
        disposables.add(
                serieEpisodeService
                        .findById(newItemId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (newItem) -> {
                                    item.setWatchingDate(newItem.getWatchingDate());
                                    notifyDataSetChanged();
                                }
                        )
        );
    }


    private boolean unregisterWatching(SerieEpisodeDto item) {
        if (item.getWatchingDate() != null) {
            disposables.add(
                    serieEpisodeService.delete(item)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                item.setWatchingDate(null);
                                notifyDataSetChanged();
                            })
            );
        }
        return true;
    }

    public void destroy() {
        for(Disposable disposable : disposables){
            disposable.dispose();
        }
    }

}
