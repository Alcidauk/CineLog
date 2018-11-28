package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;

import java.util.Comparator;

public class RatingComparator implements Comparator<SerieReview> {

    @Override
    public int compare(SerieReview o1, SerieReview o2) {
        if(o1 == null){
            return -1;
        } else if(o2 == null){
            return 1;
        }

        Review r1 = o1.getReview();
        Review r2 = o2.getReview();

        if(r1 == null){
            return -1;
        } else if(r2 == null){
            return 1;
        }

        float rating1 = r1.getRating() / r1.getMaxRating() * 100;
        float rating2 = r2.getRating() / r2.getMaxRating() * 100;

        if(rating1 == rating2){
            return 0;
        }

        return rating1 < rating2 ? -1 : 1;
    }
}
