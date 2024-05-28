package com.ulicae.cinelog.data.dao.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbReader {

    private final SQLiteDatabase db;
    private DbHelper dbHelper;

    /*

    // Define a projection that specifies which columns from the database
// you will actually use after this query.

    // Filter results WHERE "title" = 'My Title'
        String selection = KinoReaderContract.LocalKino.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"My Title"};

    // How you want the results sorted in the resulting Cursor
    //String sortOrder =
    //        KinoReaderContract.LocalKino.COLUMN_NAME_ID + " DESC";

    Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Tables:
        JOIN_LOCAL_KINO_WITH_TAG x
        JOIN_REVIEW_WITH_TAG
        LOCAL_KINO x
        sqlite_sequence
        REVIEW
        SERIE_EPISODE
        SERIE_REVIEW
        TAG x
        TMDB_KINO x
        TMDB_SERIE
        WISHLIST_MOVIE
        WISHLIST_SERIE

         */

    public DbReader(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public List<KinoDto> readSeries(List<TagDto> tags, int biggestMovieReviewId) {
        Map<Long, List<Long>> tagsByKino = readJoinReviewTag();

        Cursor cursor = db.query(
                KinoReaderContract.Review.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToNext();

        List<KinoDto> series = new ArrayList<>();

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

        Cursor cursor = db.query(
                KinoReaderContract.JoinReviewWithSerie.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            long reviewId = cursor.getLong(2);
            long tagId = cursor.getLong(1);
            if(joinReviewToTag.get(reviewId) == null){
                joinReviewToTag.put(reviewId, new ArrayList<>());
            }
            joinReviewToTag.get(reviewId).add(tagId);
            cursor.moveToNext();
        }

        return joinReviewToTag;
    }

    public List<KinoDto> readKinos(List<TagDto> tags) {
        /*
        LOCAL_KINO columns:
        _id
        TMDB_ID
        TITLE
        REVIEW_DATE
        REVIEW
        RATING
        MAX_RATING
         */

        Map<Long, List<Long>> tagsByKino = readJoinKinoTag();

        Cursor cursor = db.query(
                KinoReaderContract.LocalKino.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
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
                cursor.getLong(2) + biggestMovieReviewId, // TODO est-ce qu'on veut mettre ça ??
                cursor.getString(1),
                cursor.getString(2) != null ? new Date(cursor.getLong(2)) : null,
                cursor.getString(3),
                cursor.getFloat(4),
                cursor.getInt(5),
                null,
                null,
                0,
                null,
                serieTags
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
                serieTags
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

    private static boolean hasTmdb(Cursor cursor) {
        return cursor.getLong(1) != 0L;
    }

    public Map<Long, List<Long>> readJoinKinoTag() {
        Map<Long, List<Long>> joinKinoToTag = new HashMap<>();

        Cursor cursor = db.query(
                KinoReaderContract.JoinKinoTag.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        cursor.moveToNext();

        while (!cursor.isAfterLast()) {
            long kinoId = cursor.getLong(2);
            long tagId = cursor.getLong(1);
            if(joinKinoToTag.get(kinoId) == null){
                joinKinoToTag.put(kinoId, new ArrayList<>());
            }
            joinKinoToTag.get(kinoId).add(tagId);
            cursor.moveToNext();
        }

        return joinKinoToTag;
    }

    public List<TagDto> readTags(Context applicationContext) {
        Cursor cursor = db.query(
                KinoReaderContract.Tag.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToNext();


        List<TagDto> tags = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            tags.add(
                    new TagDto(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2), //cursor.getString(3),
                            cursor.getString(3) == "1" ? true : false,
                            cursor.getString(4) == "1" ? true : false
                    )
            );
            cursor.moveToNext();
        }

        return tags;
    }
}
