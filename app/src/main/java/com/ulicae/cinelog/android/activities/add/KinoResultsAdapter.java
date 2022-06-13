package com.ulicae.cinelog.android.activities.add;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.android.activities.EditReview;
import com.ulicae.cinelog.android.activities.ViewKino;
import com.ulicae.cinelog.android.activities.ViewUnregisteredKino;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.network.KinoBuilderFromMovie;
import com.uwetrottmann.tmdb2.entities.BaseMovie;

import org.parceler.Parcels;

import java.util.List;

import static com.ulicae.cinelog.android.activities.add.AddKino.RESULT_VIEW_KINO;

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
public class KinoResultsAdapter extends ItemResultAdapter<BaseMovie> {

    public KinoResultsAdapter(Context context, List<BaseMovie> results) {
        super(context,
                results,
                new KinoService(((KinoApplication) ((AddKino) context).getApplication()).getDaoSession()),
                new KinoBuilderFromMovie()
        );
    }

    @Override
    protected void addReview(View view, Long tmdbId, KinoDto kinoDto) {
        Intent intent = new Intent(view.getContext(), EditReview.class);

        KinoDto kinoByTmdbMovieId = dataService.getWithTmdbId(tmdbId);

        intent.putExtra("dtoType", "kino");

        if (kinoByTmdbMovieId == null) {
            intent.putExtra("kino", Parcels.wrap(kinoDto));
            intent.putExtra("creation", true);
        } else {
            intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
        }

        getContext().startActivity(intent);
    }

    @Override
    protected void viewDetails(KinoDto kinoDto, int position) {
        KinoDto kinoByTmdbMovieId = dataService.getWithTmdbId(kinoDto.getTmdbKinoId());
        if (kinoByTmdbMovieId == null) {
            Intent intent = new Intent(getContext(), ViewUnregisteredKino.class);
            intent.putExtra("dtoType", "kino");
            intent.putExtra("kino", Parcels.wrap(kinoDto));

            getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), ViewKino.class);
            intent.putExtra("dtoType", "kino");
            intent.putExtra("kino", Parcels.wrap(kinoByTmdbMovieId));
            intent.putExtra("kino_position", position);
            ((AppCompatActivity) getContext()).startActivityForResult(intent, RESULT_VIEW_KINO);
        }
    }

}
