package git.rrigby.kinolog.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.parceler.Parcel;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ryan on 10/05/17.
 */
@Parcel
@Entity
public class LocalKino {

    @Id(autoincrement = true) Long id;

    String poster_path;
    
    float  rating;
    String review;
    String overview;
    int year;

    @NotNull
    String title;
    String release_date;
    int movie_id;



    public LocalKino(){}

    public LocalKino(String title, String release_date, String poster_path, String overview, int year, int movie_id) {
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.movie_id = movie_id;
        this.rating = 0;
        this.overview = overview;
        this.year = year;
        //this.review = "";
    }

    @Generated(hash = 918668848)
    public LocalKino(Long id, String poster_path, float rating, String review, String overview, int year,
            @NotNull String title, String release_date, int movie_id) {
        this.id = id;
        this.poster_path = poster_path;
        this.rating = rating;
        this.review = review;
        this.overview = overview;
        this.year = year;
        this.title = title;
        this.release_date = release_date;
        this.movie_id = movie_id;
    }




    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return this.review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return this.release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getMovie_id() {
        return this.movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
