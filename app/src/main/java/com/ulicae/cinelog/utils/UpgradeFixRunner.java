package com.ulicae.cinelog.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ulicae.cinelog.BuildConfig;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.dto.SerieDto;

import java.util.List;

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
public class UpgradeFixRunner {

    private Context context;
    private Application application;

    public UpgradeFixRunner(Context context, Application application) {
        this.context = context;
        this.application = application;
    }

    public void runFixesIfNeeded() {
        PreferencesWrapper preferencesWrapper = new PreferencesWrapper();
        int lastCodeVersionSaved = preferencesWrapper.getIntegerPreference(context, "last_code_version_saved", 0);

        if (lastCodeVersionSaved != BuildConfig.VERSION_CODE) {
            try {
                lookForFixes(lastCodeVersionSaved);
            } catch (Exception e) {
                Log.i("upgrade_fix", "Unable to process with fixes. Won't upgrade preference version code.");
                return;
            }
            preferencesWrapper.setIntegerPreference(context, "last_code_version_saved", BuildConfig.VERSION_CODE);
        }
    }

    private void lookForFixes(int lastCodeVersionSaved) {
        if (lastCodeVersionSaved < 19 && BuildConfig.VERSION_CODE >= 19) {
            fixSerieReviews();
        }
    }

    private void fixSerieReviews() {
        SerieService serieService = new SerieService(((KinoApplication) application).getDaoSession(), context);

        List<SerieDto> all = serieService.getAll();
        for (SerieDto serieDto : all) {
            if (serieDto.getReviewId() == 0L) {
                serieDto.setReviewId(null);

                serieService.createOrUpdate(serieDto);
                Log.i("upgrade_fix", String.format("Creating own review for serie with id %s", serieDto.getTmdbKinoId()));
            }

            serieService.syncWithTmdb(serieDto.getTmdbKinoId());
            Log.i("upgrade_fix", String.format("Refreshing data of %s serie with tmdb online db", serieDto.getTmdbKinoId()));
        }

        Toast.makeText(application.getBaseContext(), application.getBaseContext().getString(R.string.restart_app_please), Toast.LENGTH_LONG).show();
    }
}
