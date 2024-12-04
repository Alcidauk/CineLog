package com.ulicae.cinelog.room.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReviewWithEpisodes {
    @Embedded
    public Review review;

    @Relation(
            parentColumn = "id",
            entityColumn = "review_id"
    )
    public List<TmdbSerieEpisode> episodes;

    public ReviewWithEpisodes(Review review, List<TmdbSerieEpisode> episodes) {
        this.review = review;
        this.episodes = episodes;
    }
}
