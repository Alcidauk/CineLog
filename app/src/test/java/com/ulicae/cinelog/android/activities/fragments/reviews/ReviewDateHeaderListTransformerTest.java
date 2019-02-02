package com.ulicae.cinelog.android.activities.fragments.reviews;

import android.content.Context;

import com.ulicae.cinelog.R;
import com.ulicae.cinelog.data.dto.KinoDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
@RunWith(MockitoJUnitRunner.class)
public class ReviewDateHeaderListTransformerTest {

    @Mock
    private KinoDto aKino;

    @Mock
    private Context context;

    private Date getDateForYear(String year) throws ParseException {
        return new SimpleDateFormat("yyyy", Locale.FRANCE).parse(year);
    }

    @Before
    public void setUp() throws ParseException {
        //noinspection ResultOfMethodCallIgnored
        doReturn(getDateForYear("2018")).when(aKino).getReview_date();
    }

    @Test
    public void transformListWithOneKino() {

        assertEquals(
                new ArrayList<Object>() {{
                    add("2018");
                    add(aKino);
                }},
                new ReviewDateHeaderListTransformer(context, new ArrayList<KinoDto>() {{
                    add(aKino);
                }}).transform()
        );

    }

    @Test
    public void transformListWithSeveralKinos() throws ParseException {
        doReturn("Année inconnue").when(context).getString(R.string.unknown_year);

        final KinoDto anotherKino = mock(KinoDto.class);
        final KinoDto aThirdKino = mock(KinoDto.class);
        final KinoDto aFourthKino = mock(KinoDto.class);

        //noinspection ResultOfMethodCallIgnored
        doReturn(getDateForYear("2015")).when(anotherKino).getReview_date();
        //noinspection ResultOfMethodCallIgnored
        doReturn(getDateForYear("2015")).when(aThirdKino).getReview_date();
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(aFourthKino).getReview_date();

        assertEquals(
                new ArrayList<Object>() {{
                    add("2018");
                    add(aKino);
                    add("2015");
                    add(anotherKino);
                    add(aThirdKino);
                    add("Année inconnue");
                    add(aFourthKino);
                }},
                new ReviewDateHeaderListTransformer(context, new ArrayList<KinoDto>() {{
                    add(aKino);
                    add(anotherKino);
                    add(aThirdKino);
                    add(aFourthKino);
                }}).transform()
        );

    }
}