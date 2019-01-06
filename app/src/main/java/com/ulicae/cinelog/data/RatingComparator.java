package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;

import java.util.Comparator;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
