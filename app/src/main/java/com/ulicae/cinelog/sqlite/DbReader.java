package com.ulicae.cinelog.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dto.SerieEpisodeDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.dto.data.WishlistDataDto;
import com.ulicae.cinelog.room.dto.data.WishlistItemType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbReader {

    private final SQLiteDatabase db;
    private DbHelper dbHelper;

    public DbReader(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public List<KinoDto> readSeries(List<TagDto> tags, int biggestMovieReviewId) {
        Map<Long, List<Long>> tagsByKino = readJoinReviewTag();
        List<KinoDto> series = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.Review.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return series;
        }

        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            List<TagDto> serieTags = tags
                    .stream()
                    .filter(
                            (tagDto ->
                                    tagsByKino.get(cursor.getLong(0)) != null &&
                                            tagsByKino.get(cursor.getLong(0)).contains(tagDto.getId())))
                    .collect(Collectors.toList());

            Cursor tmdbSerieCursor = serieTmdbEntity(cursor);
            series.add(tmdbSerieCursor != null ?
                    buildSerieDtoWithTmdb(cursor, tmdbSerieCursor, serieTags, biggestMovieReviewId) :
                    buildSerieDto(cursor, serieTags, biggestMovieReviewId));

            cursor.moveToNext();
        }

        return series;
    }

    private Map<Long, List<Long>> readJoinReviewTag() {
        Map<Long, List<Long>> joinReviewToTag = new HashMap<>();

        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.JoinReviewWithSerie.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return joinReviewToTag;
        }

        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            long reviewId = cursor.getLong(2);
            long tagId = cursor.getLong(1);
            if (joinReviewToTag.get(reviewId) == null) {
                joinReviewToTag.put(reviewId, new ArrayList<>());
            }
            joinReviewToTag.get(reviewId).add(tagId);
            cursor.moveToNext();
        }

        return joinReviewToTag;
    }

    public List<KinoDto> readKinos(List<TagDto> tags) {
        Map<Long, List<Long>> tagsByKino = readJoinKinoTag();

        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.LocalKino.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return new ArrayList<>();
        }

        cursor.moveToNext();

        List<KinoDto> kinos = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            List<TagDto> kinoTags = tags
                    .stream()
                    .filter(
                            (tagDto ->
                                    tagsByKino.get(cursor.getLong(0)) != null &&
                                            tagsByKino.get(cursor.getLong(0)).contains(tagDto.getId())))
                    .collect(Collectors.toList());

            kinos.add(hasTmdb(cursor) ? buildKinoDtoWithTmdb(cursor, kinoTags) : buildKinoDto(cursor, kinoTags));

            cursor.moveToNext();
        }

        return kinos;
    }

    @NonNull
    private static KinoDto buildKinoDto(Cursor cursor, List<TagDto> kinoTags) {
        return new KinoDto(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3) != null ? new Date(cursor.getLong(3)) : null,
                cursor.getString(4),
                cursor.getFloat(5),
                cursor.getInt(6),
                null,
                null,
                0,
                null,
                kinoTags
        );
    }

    @NonNull
    private static SerieDto buildSerieDto(Cursor cursor, List<TagDto> serieTags, int biggestMovieReviewId) {
        return new SerieDto(
                cursor.getLong(0),
                0L,
                null,
                cursor.getString(1),
                cursor.getString(2) != null ? new Date(cursor.getLong(2)) : null,
                cursor.getString(3),
                cursor.getFloat(4),
                cursor.getInt(5),
                null,
                null,
                0,
                null,
                serieTags,
                null // Espisodes are built after an then linked
        );
    }

    @NonNull
    private KinoDto buildKinoDtoWithTmdb(Cursor cursor, List<TagDto> kinoTags) {
        String[] tmdbId = {cursor.getString(1)};

        Cursor kinoCursor = db.query(
                KinoReaderContract.TmdbKino.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                KinoReaderContract.TmdbKino.COLUMN_NAME_ID + "=?",
                tmdbId,
                null,
                null,
                null
        );
        kinoCursor.moveToNext();

        return new KinoDto(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3) != null ? new Date(cursor.getLong(3)) : null,
                cursor.getString(4),
                cursor.getFloat(5),
                cursor.getInt(6),
                kinoCursor.getString(1),
                kinoCursor.getString(2),
                kinoCursor.getInt(3),
                kinoCursor.getString(4),
                kinoTags
        );
    }

    private SerieDto buildSerieDtoWithTmdb(Cursor cursor, Cursor tmdbSerieCursor, List<TagDto> serieTags, int biggestMovieReviewId) {
        String[] tmdbId = {tmdbSerieCursor.getString(1)};

        Cursor tmdbCursor = db.query(
                KinoReaderContract.TmdbSerie.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                KinoReaderContract.TmdbSerie.COLUMN_NAME_ID + "=?",
                tmdbId,
                null,
                null,
                null
        );
        tmdbCursor.moveToNext();

        return new SerieDto(
                tmdbSerieCursor.getLong(0),
                tmdbSerieCursor.getLong(1),
                tmdbSerieCursor.getLong(2) + biggestMovieReviewId,
                cursor.getString(1),
                cursor.getString(2) != null ? new Date(cursor.getLong(2)) : null,
                cursor.getString(3),
                cursor.getFloat(4),
                cursor.getInt(5),
                tmdbCursor.getString(1),
                tmdbCursor.getString(2),
                tmdbCursor.getInt(3),
                tmdbCursor.getString(4),
                serieTags,
                null // Espisodes are built after an then linked
        );
    }

    private Cursor serieTmdbEntity(Cursor cursor) {
        String[] reviewId = {cursor.getString(0)};

        Cursor serieReviewCursor = db.query(
                KinoReaderContract.SerieReview.TABLE_NAME,
                null,
                KinoReaderContract.SerieReview.COLUMN_NAME_REVIEW_ID + "=?",
                reviewId,
                null,
                null,
                null
        );
        serieReviewCursor.moveToNext();

        return serieReviewCursor.isAfterLast() || serieReviewCursor.getInt(1) == 0 ? null : serieReviewCursor;
    }

    public List<SerieEpisodeDto> readSerieEpisodes() {
        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.SerieEpisode.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return new ArrayList<>();
        }

        cursor.moveToNext();

        List<SerieEpisodeDto> episodes = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            episodes.add(buildEpisodeDto(cursor));
            cursor.moveToNext();
        }

        return episodes;
    }

    private SerieEpisodeDto buildEpisodeDto(Cursor cursor) {
        return new SerieEpisodeDto(
                cursor.getInt(1),
                cursor.getLong(2),
                null,
                new Date(cursor.getLong(3)),
                null,
                null,
                null,
                null
        );
    }

    private static boolean hasTmdb(Cursor cursor) {
        return cursor.getLong(1) != 0L;
    }

    public Map<Long, List<Long>> readJoinKinoTag() {
        Map<Long, List<Long>> joinKinoToTag = new HashMap<>();

        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.JoinKinoTag.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return joinKinoToTag;
        }
        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            long kinoId = cursor.getLong(2);
            long tagId = cursor.getLong(1);
            if (joinKinoToTag.get(kinoId) == null) {
                joinKinoToTag.put(kinoId, new ArrayList<>());
            }
            joinKinoToTag.get(kinoId).add(tagId);
            cursor.moveToNext();
        }

        return joinKinoToTag;
    }

    public List<TagDto> readTags(Context applicationContext) {
        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.Tag.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return new ArrayList<>();
        }

        cursor.moveToNext();


        List<TagDto> tags = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            tags.add(
                    new TagDto(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2), //cursor.getString(3),
                            cursor.getString(3).equals("1") ? true : false,
                            cursor.getString(4).equals("1") ? true : false
                    )
            );
            cursor.moveToNext();
        }

        return tags;
    }

    public List<WishlistDataDto> readWishlistMovieItems() {
        List<WishlistDataDto> wishlistItems = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.WishlistMovieItem.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return wishlistItems;
        }

        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            wishlistItems.add(cursor.getInt(1) != 0 ?
                    buildWishlistDtoWithTmdb(cursor) :
                    buildWishlistDto(cursor, WishlistItemType.MOVIE, 0));

            cursor.moveToNext();
        }

        return wishlistItems;
    }

    private WishlistDataDto buildWishlistDto(Cursor cursor, WishlistItemType wishlistItemType, int biggestMovieReviewId) {
        return new WishlistDataDto(
                cursor.getLong(0) + biggestMovieReviewId,
                cursor.getLong(1),
                cursor.getString(2),
                null,
                null,
                0,
                null,
                wishlistItemType
        );
    }

    private WishlistDataDto buildWishlistDtoWithTmdb(Cursor cursor) {
        String[] tmdbId = {cursor.getString(1)};

        Cursor tmdbCursor = db.query(
                KinoReaderContract.TmdbKino.TABLE_NAME,
                null,
                KinoReaderContract.TmdbKino.COLUMN_NAME_ID + "=?",
                tmdbId,
                null,
                null,
                null
        );
        tmdbCursor.moveToNext();

        return new WishlistDataDto(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                tmdbCursor.getString(1),
                tmdbCursor.getString(2),
                tmdbCursor.getInt(3),
                tmdbCursor.getString(4),
                WishlistItemType.MOVIE
        );
    }

    public List<WishlistDataDto> readWishlistSerieItems(int biggestMovieReviewId) {
        Cursor cursor;
        try {
            cursor = this.getTableCursor(KinoReaderContract.WishlistSerieItem.TABLE_NAME);
        } catch (NoSuchTableException e) {
            return new ArrayList<>();
        }

        cursor.moveToNext();

        List<WishlistDataDto> wishlistItems = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            wishlistItems.add(cursor.getInt(1) != 0 ?
                    buildWishlistSerieDtoWithTmdb(cursor, biggestMovieReviewId) :
                    buildWishlistDto(cursor, WishlistItemType.SERIE, biggestMovieReviewId));

            cursor.moveToNext();
        }

        return wishlistItems;
    }

    private WishlistDataDto buildWishlistSerieDtoWithTmdb(Cursor cursor, int biggestMovieReviewId) {
        String[] tmdbId = {cursor.getString(1)};

        Cursor tmdbCursor = db.query(
                KinoReaderContract.TmdbSerie.TABLE_NAME,
                null,
                KinoReaderContract.TmdbSerie.COLUMN_NAME_ID + "=?",
                tmdbId,
                null,
                null,
                null
        );
        tmdbCursor.moveToNext();

        return new WishlistDataDto(
                cursor.getLong(0) + biggestMovieReviewId,
                cursor.getLong(1),
                cursor.getString(2),
                tmdbCursor.getString(1),
                tmdbCursor.getString(2),
                tmdbCursor.getInt(3),
                tmdbCursor.getString(4),
                WishlistItemType.SERIE
        );
    }

    private Cursor getTableCursor(String tableName) throws NoSuchTableException {
        Cursor cursor;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                throw new NoSuchTableException();
            } else {
                throw e;
            }
        }
        return cursor;
    }
}
