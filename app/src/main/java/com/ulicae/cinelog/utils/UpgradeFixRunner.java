package com.ulicae.cinelog.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.ulicae.cinelog.BuildConfig;
import com.ulicae.cinelog.KinoApplication;
import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;
import com.ulicae.cinelog.data.services.tags.TagService;
import com.ulicae.cinelog.data.services.wishlist.MovieWishlistService;
import com.ulicae.cinelog.data.services.wishlist.SerieWishlistService;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.utils.room.ReviewFromDtoCreator;
import com.ulicae.cinelog.utils.room.ReviewTmdbCrossRefFromDtoCreator;
import com.ulicae.cinelog.utils.room.SerieReviewFromDtoCreator;
import com.ulicae.cinelog.utils.room.SerieReviewTmdbCrossRefFromDtoCreator;
import com.ulicae.cinelog.utils.room.SerieTmdbFromDtoCreator;
import com.ulicae.cinelog.utils.room.TagFromDtoCreator;
import com.ulicae.cinelog.utils.room.TagReviewCrossRefFromDtoCreator;
import com.ulicae.cinelog.utils.room.TmdbFromDtoCreator;
import com.ulicae.cinelog.utils.room.WishlistFromDtoCreator;
import com.ulicae.cinelog.utils.room.WishlistTmdbCrossRefFromDtoCreator;
import com.ulicae.cinelog.utils.room.WishlistTmdbFromDtoCreator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    private void migrateToRoom() {
        AppDatabase db = Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class, "database-cinelog").build();

        disposables.add(
                Observable.just(db)
                        .subscribeOn(Schedulers.io())
                        .subscribe(givenDb -> {
                            givenDb.clearAllTables();

                            int biggestMovieReviewId = getBiggestMovieId();

                            migrateTags(givenDb);

                            migrateMovieReviews(givenDb);
                            migrateSerieReviews(givenDb, biggestMovieReviewId);
                            migrateWishlistItems(givenDb, biggestMovieReviewId);
                        })
        );
    }

    private int getBiggestMovieId() {
        return new KinoService(((KinoApplication) application).getDaoSession()).getBiggestKinoId();
    }

    private void migrateTags(AppDatabase givenDb) {
        TagFromDtoCreator tagFromDtoCreator = new TagFromDtoCreator(givenDb.tagDao());

        List<TagDto> tagDtos =
                new TagService(((KinoApplication) application).getDaoSession()).getAll();

        tagFromDtoCreator.insertAll(tagDtos);
    }

    private void migrateWishlistItems(AppDatabase givenDb, int biggestMovieReviewId) {
        WishlistFromDtoCreator serieWishlistFromDtoCreator =
                new WishlistFromDtoCreator(givenDb.wishlistItemDao(), ItemEntityType.SERIE, biggestMovieReviewId);
        WishlistTmdbCrossRefFromDtoCreator serieWishlistTmdbCrossRefFromDtoCreator =
                new WishlistTmdbCrossRefFromDtoCreator(givenDb.wishlistTmdbCrossRefDao(), ItemEntityType.SERIE, biggestMovieReviewId);

        WishlistFromDtoCreator movieWishlistFromDtoCreator =
                new WishlistFromDtoCreator(givenDb.wishlistItemDao(), ItemEntityType.MOVIE, biggestMovieReviewId);
        WishlistTmdbCrossRefFromDtoCreator movieWishlistTmdbCrossRefFromDtoCreator =
                new WishlistTmdbCrossRefFromDtoCreator(givenDb.wishlistTmdbCrossRefDao(), ItemEntityType.MOVIE, biggestMovieReviewId);

        WishlistTmdbFromDtoCreator wishlistTmdbFromDtoCreator =
                new WishlistTmdbFromDtoCreator(givenDb.tmdbDao());

        List<WishlistDataDto> wishlistMovieDtos =
                new MovieWishlistService(((KinoApplication) application).getDaoSession()).getAll();

        movieWishlistFromDtoCreator.insertAll(wishlistMovieDtos);
        wishlistTmdbFromDtoCreator.insertAll(wishlistMovieDtos);
        movieWishlistTmdbCrossRefFromDtoCreator.insertAll(wishlistMovieDtos);

        List<WishlistDataDto> wishlistSerieDtos =
                new SerieWishlistService(((KinoApplication) application).getDaoSession()).getAll();

        serieWishlistFromDtoCreator.insertAll(wishlistSerieDtos);
        wishlistTmdbFromDtoCreator.insertAll(wishlistSerieDtos);
        serieWishlistTmdbCrossRefFromDtoCreator.insertAll(wishlistSerieDtos);
    }

    private void migrateSerieReviews(AppDatabase givenDb, int biggestMovieReviewId) {
        SerieReviewFromDtoCreator serieReviewFromDtoCreator =
                new SerieReviewFromDtoCreator(givenDb.reviewDao(), biggestMovieReviewId);
        SerieReviewTmdbCrossRefFromDtoCreator serieReviewTmdbCrossRefFromDtoCreator =
                new SerieReviewTmdbCrossRefFromDtoCreator(givenDb.reviewTmdbDao(), biggestMovieReviewId);
        SerieTmdbFromDtoCreator serieTmdbFromDtoCreator =
                new SerieTmdbFromDtoCreator(givenDb.tmdbDao());

        List<SerieDto> serieDtos =
                new SerieService(((KinoApplication) application).getDaoSession(), application.getApplicationContext()).getAll();

        serieReviewFromDtoCreator.insertAll(serieDtos);
        serieTmdbFromDtoCreator.insertAll(serieDtos);
        serieReviewTmdbCrossRefFromDtoCreator.insertAll(serieDtos);

        for(SerieDto serieDto : serieDtos) {
            TagReviewCrossRefFromDtoCreator tagReviewCrossRefFromDtoCreator =
                    new TagReviewCrossRefFromDtoCreator(givenDb.reviewTagCrossRefDao(), serieDto, biggestMovieReviewId);
            tagReviewCrossRefFromDtoCreator.insertAll(serieDto.getTags());
        }
    }

    private void migrateMovieReviews(AppDatabase givenDb) {
        ReviewFromDtoCreator reviewFromDtoCreator =
                new ReviewFromDtoCreator(givenDb.reviewDao());
        ReviewTmdbCrossRefFromDtoCreator reviewTmdbCrossRefFromDtoCreator =
                new ReviewTmdbCrossRefFromDtoCreator(givenDb.reviewTmdbDao());
        TmdbFromDtoCreator tmdbFromDtoCreator =
                new TmdbFromDtoCreator(givenDb.tmdbDao());

        List<KinoDto> kinoDtos =
                new KinoService(((KinoApplication) application).getDaoSession()).getAll();

        reviewFromDtoCreator.insertAll(kinoDtos);
        tmdbFromDtoCreator.insertAll(kinoDtos);
        reviewTmdbCrossRefFromDtoCreator.insertAll(kinoDtos);

        for(KinoDto kinoDto : kinoDtos) {
            TagReviewCrossRefFromDtoCreator tagReviewCrossRefFromDtoCreator =
                    new TagReviewCrossRefFromDtoCreator(givenDb.reviewTagCrossRefDao(), kinoDto, 0);
            tagReviewCrossRefFromDtoCreator.insertAll(kinoDto.getTags());
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
