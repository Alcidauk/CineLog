package com.ulicae.cinelog.data.dao.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.data.dto.KinoDto;
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
                KinoReaderContract.LocalKino.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
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
                KinoReaderContract.Tag.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
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
