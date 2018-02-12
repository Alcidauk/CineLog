package com.alcidauk.cinelog;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

/**
 * Created by alcidauk on 29/01/18.
 */

public class ExportLocalKino {

    private Long id;

    private String poster_path;

    private float rating;
    private String review;
    private String overview;
    private int year;

    private String title;
    private String release_date;
    private int movie_id;

    private Date review_date;

    public ExportLocalKino(Long id, String poster_path, float rating, String review, String overview, int year, String title, String release_date, int movie_id, Date review_date) {
        this.id = id;
        this.poster_path = poster_path;
        this.rating = rating;
        this.review = review;
        this.overview = overview;
        this.year = year;
        this.title = title;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.review_date = review_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public Date getReview_date() {
        return review_date;
    }

    public void setReview_date(Date review_date) {
        this.review_date = review_date;
    }
}
