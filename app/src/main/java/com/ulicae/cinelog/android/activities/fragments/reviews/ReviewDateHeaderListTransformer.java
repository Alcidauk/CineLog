package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * CineLog Copyright 2019 Pierre Rognon
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
class ReviewDateHeaderListTransformer {

    private Context context;
    private List<KinoDto> kinoDtos;

    ReviewDateHeaderListTransformer(Context context, List<KinoDto> kinoDtos) {
        this.context = context;
        this.kinoDtos = kinoDtos;
    }

    List<Object> transform() {
        final ArrayList<Object> objects = new ArrayList<Object>(kinoDtos);

        String currentYear = null;

        SortedMap<Integer, String> yearsToInsert = new TreeMap<>();
        int indexOfObjet = 0;
        for (Object object : objects) {
            String releaseDateYear = String.valueOf(getYearFromReviewDate(((KinoDto) object).getReview_date()));
            if (!releaseDateYear.equals(currentYear)) {
                yearsToInsert.put(indexOfObjet, releaseDateYear);
                currentYear = releaseDateYear;
            }
            indexOfObjet++;
        }

        int offset = 0;
        for (Integer index : yearsToInsert.keySet()) {
            String yearToInsert = yearsToInsert.get(index);
            objects.add(index + offset, yearToInsert);
            ++offset;
        }

        return objects;
    }

    private String getYearFromReviewDate(Date reviewDate){
        if(reviewDate != null){
            return new SimpleDateFormat("yyyy", Locale.FRANCE).format(reviewDate);
        }

        return context.getString(R.string.unknown_year);
    }
}
