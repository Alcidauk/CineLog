package com.ulicae.cinelog.data.dao.sqlite;

import android.provider.BaseColumns;

public final class KinoReaderContract {

    private KinoReaderContract() {}

    // MOVIE

    public static class LocalKino implements BaseColumns {
        public static final String TABLE_NAME = "LOCAL_KINO";
    }

    public static class TmdbKino implements BaseColumns {
        public static final String TABLE_NAME = "TMDB_KINO";
        public static final String COLUMN_NAME_ID = "_id";
    }

    // SERIE

    public static class Review implements BaseColumns {
        public static final String TABLE_NAME = "REVIEW";
    }

    public static class SerieReview implements BaseColumns {
        public static final String TABLE_NAME = "SERIE_REVIEW";
        public static final String COLUMN_NAME_REVIEW_ID = "REVIEW_id";
    }

    public static class SerieEpisode implements BaseColumns {
        public static final String TABLE_NAME = "SERIE_EPISODE";
    }

    public static class TmdbSerie implements BaseColumns {
        public static final String TABLE_NAME = "TMDB_SERIE";
        public static final String COLUMN_NAME_ID = "_id";
    }

    public static class JoinReviewWithSerie implements BaseColumns {
        public static final String TABLE_NAME = "JOIN_REVIEW_WITH_TAG";
    }

    // TAG

    public static class Tag implements BaseColumns {
        public static final String TABLE_NAME = "TAG";
    }

    public static class JoinKinoTag implements BaseColumns {
        public static final String TABLE_NAME = "JOIN_LOCAL_KINO_WITH_TAG";
    }

    public static class WishlistMovieItem implements BaseColumns {
        public static final String TABLE_NAME = "WISHLIST_MOVIE";
    }
    public static class WishlistSerieItem implements BaseColumns {
        public static final String TABLE_NAME = "WISHLIST_SERIE";
    }
}
