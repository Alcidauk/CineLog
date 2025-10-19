package com.ulicae.cinelog.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.ulicae.cinelog.BuildConfig;
import com.ulicae.cinelog.room.AppDatabase;
import com.ulicae.cinelog.room.dto.ItemDto;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieEpisodeDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dto.utils.from.ReviewFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.from.SerieEpisodeFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.from.TagFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.from.TagReviewCrossRefFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.from.WishlistFromDtoCreator;
import com.ulicae.cinelog.sqlite.DbReader;

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
        if (lastCodeVersionSaved == 0) {
            return;
        }

        if (lastCodeVersionSaved < 41 && BuildConfig.VERSION_CODE >= 41) {
            migrateToRoom();
        }
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
                            List<KinoDto> serieDtos = migrateSerieReviews(givenDb, createdTags, biggestMovieReviewId);

                            migrateEpisodes(givenDb, serieDtos);

                            migrateWishlistItems(givenDb);
                        })
        );
    }

    private void migrateEpisodes(AppDatabase givenDb, List<KinoDto> serieDtos) {
        List<SerieEpisodeDto> episodeDtos = new DbReader(application.getApplicationContext())
                .readSerieEpisodes();

        SerieEpisodeFromDtoCreator episodeFromDtoCreator =
                new SerieEpisodeFromDtoCreator(
                        givenDb.syncTmdbSerieEpisodeDao(),
                        serieDtos
                );
        episodeFromDtoCreator.insertAll(episodeDtos);
    }

    private int getBiggestId(List<? extends ItemDto> kinoDtos) {
        List<? extends ItemDto> kinoList = kinoDtos.stream()
                .sorted((dto1, dto2) -> dto1.getId() < dto2.getId() ? 1 : -1)
                .collect(Collectors.toList());
        return kinoList.size() > 0 ?
                Math.toIntExact(kinoList.get(0).getId()) :
                0;
    }

    private List<TagDto> migrateTags(AppDatabase givenDb) {
        TagFromDtoCreator tagFromDtoCreator = new TagFromDtoCreator(givenDb.tagDao());

        List<TagDto> tagDtos = new DbReader(application.getApplicationContext()).readTags(application.getApplicationContext());

        tagFromDtoCreator.insertAllForMigration(tagDtos);

        return tagDtos;
    }

    private void migrateWishlistItems(AppDatabase givenDb) {
        List<WishlistDataDto> wishlistDtos = new DbReader(application.getApplicationContext()).readWishlistMovieItems();

        int biggestMovieReviewId = getBiggestId(wishlistDtos);

        List<WishlistDataDto> wishlistSerieDtos = new DbReader(application.getApplicationContext()).readWishlistSerieItems(biggestMovieReviewId);

        WishlistFromDtoCreator wishlistFromDtoCreator =
                new WishlistFromDtoCreator(givenDb.syncWishlistItemDao());

        wishlistDtos.addAll(wishlistSerieDtos);

        wishlistFromDtoCreator.insertAll(wishlistDtos);
    }

    private List<KinoDto> migrateSerieReviews(AppDatabase givenDb,
                                              List<TagDto> createdTags,
                                              int biggestMovieReviewId) {
        ReviewFromDtoCreator reviewFromDtoCreator =
                new ReviewFromDtoCreator(givenDb.reviewDao(), biggestMovieReviewId);

        List<KinoDto> serieDtos =
                new DbReader(application.getApplicationContext()).readSeries(
                        createdTags,
                        biggestMovieReviewId);

        for (KinoDto kinoDto : serieDtos) {
            reviewFromDtoCreator.insert(kinoDto);
        }

        migrateTagsOnReview(givenDb, serieDtos, biggestMovieReviewId);

        return serieDtos;
    }

    private List<KinoDto> migrateMovieReviews(AppDatabase givenDb, List<TagDto> tags) {
        ReviewFromDtoCreator reviewFromDtoCreator =
                new ReviewFromDtoCreator(givenDb.reviewDao());

        List<KinoDto> kinoDtos = new DbReader(application.getApplicationContext()).readKinos(tags);

        for (KinoDto kinoDto : kinoDtos) {
            reviewFromDtoCreator.insert(kinoDto);
        }

        migrateTagsOnReview(givenDb, kinoDtos, 0);

        return kinoDtos;
    }

    private static void migrateTagsOnReview(AppDatabase givenDb, List<KinoDto> kinoDtos, int biggestMovieReviewId) {
        for (KinoDto kinoDto : kinoDtos) {
            if (kinoDto.getTags() != null && !kinoDto.getTags().isEmpty()) {
                TagReviewCrossRefFromDtoCreator tagReviewCrossRefFromDtoCreator =
                        new TagReviewCrossRefFromDtoCreator(givenDb.reviewTagCrossRefDao(), kinoDto, biggestMovieReviewId);
                tagReviewCrossRefFromDtoCreator.insertAll(kinoDto.getTags()).subscribe();
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
