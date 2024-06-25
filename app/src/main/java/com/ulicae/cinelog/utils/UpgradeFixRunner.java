package com.ulicae.cinelog.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.ulicae.cinelog.BuildConfig;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dao.sqlite.DbReader;
import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ReviewTmdbCrossRef;
import com.ulicae.cinelog.room.entities.WishlistTmdbCrossRef;
import com.ulicae.cinelog.utils.room.ReviewFromDtoCreator;
import com.ulicae.cinelog.utils.room.TagFromDtoCreator;
import com.ulicae.cinelog.utils.room.TagReviewCrossRefFromDtoCreator;
import com.ulicae.cinelog.utils.room.TmdbFromDtoCreator;
import com.ulicae.cinelog.utils.room.WishlistFromDtoCreator;
import com.ulicae.cinelog.utils.room.WishlistTmdbFromDtoCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
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
public class UpgradeFixRunner {

    private Context context;
    private Application application;

    private List<Disposable> disposables;


    public UpgradeFixRunner(Context context, Application application) {
        this.context = context;
        this.application = application;
        this.disposables = new ArrayList<>();
    }

    public void runFixesIfNeeded() {
        PreferencesWrapper preferencesWrapper = new PreferencesWrapper();
        int lastCodeVersionSaved = preferencesWrapper.getIntegerPreference(context, "last_code_version_saved", 0);

        //if (lastCodeVersionSaved != BuildConfig.VERSION_CODE) {
        try {
            lookForFixes(lastCodeVersionSaved);
        } catch (Exception e) {
            Log.i("upgrade_fix", "Unable to process with fixes. Won't upgrade preference version code.");
            return;
        }
        preferencesWrapper.setIntegerPreference(context, "last_code_version_saved", BuildConfig.VERSION_CODE);
        //}
    }

    private void lookForFixes(int lastCodeVersionSaved) {
        migrateToRoom();

        if (lastCodeVersionSaved == 0) {
            return;
        }

        if (lastCodeVersionSaved < 19 && BuildConfig.VERSION_CODE >= 19) {
            fixSerieReviews();
        } else if (lastCodeVersionSaved < 41 && BuildConfig.VERSION_CODE >= 41) {
            migrateToRoom();
        }
    }

    private void fixSerieReviews() {
        KinoApplication app = (KinoApplication) application;
        SerieService serieService = new SerieService(
                app.getDaoSession(),
                app.getDb(),
                context
        );

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

    private void migrateToRoom() {
        AppDatabase db = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "database-cinelog")
                .fallbackToDestructiveMigration()
                .build();

        disposables.add(
                Observable.just(db)
                        .subscribeOn(Schedulers.io())
                        .subscribe(givenDb -> {
                            givenDb.clearAllTables();

                            List<TagDto> createdTags = migrateTags(givenDb);

                            List<KinoDto> kinoDtos = migrateMovieReviews(givenDb, createdTags);

                            // We need the biggest kino id to generate next serie id
                            int biggestMovieReviewId = getBiggestId(kinoDtos);
                            migrateSerieReviews(givenDb, createdTags, biggestMovieReviewId);

                            migrateWishlistItems(givenDb);
                        })
        );
    }

    private int getBiggestId(List<? extends ItemDto> kinoDtos) {
        return Math.toIntExact(
                kinoDtos.stream()
                        .sorted((dto1, dto2) -> dto1.getId() < dto2.getId() ? 1 : -1)
                        .collect(Collectors.toList())
                        .get(0)
                        .getId()
        );
    }

    private List<TagDto> migrateTags(AppDatabase givenDb) {
        TagFromDtoCreator tagFromDtoCreator = new TagFromDtoCreator(givenDb.tagDao());

        List<TagDto> tagDtos = new DbReader(application.getApplicationContext()).readTags(application.getApplicationContext());

        tagFromDtoCreator.insertAll(tagDtos);

        return tagDtos;
    }

    private void migrateWishlistItems(AppDatabase givenDb) {
        WishlistTmdbFromDtoCreator wishlistTmdbFromDtoCreator =
                new WishlistTmdbFromDtoCreator(givenDb.tmdbDao());

        List<WishlistDataDto> wishlistDtos = new DbReader(application.getApplicationContext()).readWishlistMovieItems();

        int biggestMovieReviewId = getBiggestId(wishlistDtos);

        List<WishlistDataDto> wishlistSerieDtos = new DbReader(application.getApplicationContext()).readWishlistSerieItems(biggestMovieReviewId);

        WishlistFromDtoCreator wishlistFromDtoCreator =
                new WishlistFromDtoCreator(givenDb.syncWishlistItemDao());

        wishlistDtos.addAll(wishlistSerieDtos);

        for (WishlistDataDto dto : wishlistDtos) {
            long tmdbId = wishlistTmdbFromDtoCreator.insert(dto);
            long wishlistId = wishlistFromDtoCreator.insert(dto);

            if (tmdbId != 0L) {
                givenDb.wishlistTmdbCrossRefDao().insert(new WishlistTmdbCrossRef(wishlistId, tmdbId));
            }
        }
    }

    private void migrateSerieReviews(AppDatabase givenDb, List<TagDto> createdTags, int biggestMovieReviewId) {
        ReviewFromDtoCreator reviewFromDtoCreator =
                new ReviewFromDtoCreator(givenDb.reviewDao(), biggestMovieReviewId);
        TmdbFromDtoCreator tmdbFromDtoCreator =
                new TmdbFromDtoCreator(givenDb.tmdbDao());

        List<KinoDto> serieDtos = new DbReader(application.getApplicationContext()).readSeries(createdTags, biggestMovieReviewId);

        for (KinoDto kinoDto : serieDtos) {
            long review = reviewFromDtoCreator.insert(kinoDto);
            long tmdb = tmdbFromDtoCreator.insert(kinoDto);

            if (tmdb != 0L) {
                givenDb.reviewTmdbDao().insert(new ReviewTmdbCrossRef(review, tmdb));
            }
        }

        migrateTagsOnReview(givenDb, serieDtos, biggestMovieReviewId);
    }

    private List<KinoDto> migrateMovieReviews(AppDatabase givenDb, List<TagDto> tags) {
        ReviewFromDtoCreator reviewFromDtoCreator =
                new ReviewFromDtoCreator(givenDb.reviewDao());
        TmdbFromDtoCreator tmdbFromDtoCreator =
                new TmdbFromDtoCreator(givenDb.tmdbDao());

        List<KinoDto> kinoDtos = new DbReader(application.getApplicationContext()).readKinos(tags);

        for (KinoDto kinoDto : kinoDtos) {
            long review = reviewFromDtoCreator.insert(kinoDto);
            long tmdb = tmdbFromDtoCreator.insert(kinoDto);

            if (tmdb != 0L) {
                givenDb.reviewTmdbDao().insert(new ReviewTmdbCrossRef(review, tmdb));
            }
        }

        migrateTagsOnReview(givenDb, kinoDtos, 0);

        return kinoDtos;
    }

    private static void migrateTagsOnReview(AppDatabase givenDb, List<KinoDto> kinoDtos, int biggestMovieReviewId) {
        for (KinoDto kinoDto : kinoDtos) {
            if (kinoDto.getTags() != null && !kinoDto.getTags().isEmpty()) {
                TagReviewCrossRefFromDtoCreator tagReviewCrossRefFromDtoCreator =
                        new TagReviewCrossRefFromDtoCreator(givenDb.reviewTagCrossRefDao(), kinoDto, biggestMovieReviewId);
                tagReviewCrossRefFromDtoCreator.insertAll(kinoDto.getTags());
            }
        }
    }

    public void clear() {
        for (Disposable disposable : this.disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
