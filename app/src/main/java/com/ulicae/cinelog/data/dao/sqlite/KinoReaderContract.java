package com.ulicae.cinelog.data.dao.sqlite;

import android.provider.BaseColumns;

public final class KinoReaderContract {

    private KinoReaderContract() {}

    public static class LocalKino implements BaseColumns {
        public static final String TABLE_NAME = "LOCAL_KINO";
    }

    public static class TmdbKino implements BaseColumns {
        public static final String TABLE_NAME = "TMDB_KINO";
        public static final String COLUMN_NAME_ID = "_id";
    }

    public static class Tag implements BaseColumns {
        public static final String TABLE_NAME = "TAG";
    }

    public static class JoinKinoTag implements BaseColumns {
        public static final String TABLE_NAME = "JOIN_LOCAL_KINO_WITH_TAG";
    }
}
