package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.Review;
import com.ulicae.cinelog.data.dao.SerieReview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RatingComparatorTest {

    @Mock
    private SerieReview aSerieReview;

    @Mock
    private SerieReview anotherSerieReview;

    @Mock
    private Review aReview;
    @Mock
    private Review anotherReview;

    @Before
    public void setUp() {
        doReturn(aReview).when(aSerieReview).getReview();
        doReturn(anotherReview).when(anotherSerieReview).getReview();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareNullSerieReview1() {
        assertEquals(-1, new RatingComparator().compare(null, anotherSerieReview));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareNullSerieReview2() {
        assertEquals(1, new RatingComparator().compare(anotherSerieReview, null));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareNullReview1() {
        doReturn(null).when(aSerieReview).getReview();
        doReturn(anotherReview).when(anotherSerieReview).getReview();

        assertEquals(-1, new RatingComparator().compare(aSerieReview, anotherSerieReview));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareNullReview2() {
        doReturn(aReview).when(aSerieReview).getReview();
        doReturn(null).when(anotherSerieReview).getReview();

        assertEquals(1, new RatingComparator().compare(aSerieReview, anotherSerieReview));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareReview1Worse() {
        doReturn(5f).when(aReview).getRating();
        doReturn(10).when(aReview).getMaxRating();

        doReturn(10f).when(anotherReview).getRating();
        doReturn(30).when(anotherReview).getMaxRating();

        assertEquals(1, new RatingComparator().compare(aSerieReview, anotherSerieReview));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareReview2Worse() {
        doReturn(5f).when(aReview).getRating();
        doReturn(10).when(aReview).getMaxRating();

        doReturn(6f).when(anotherReview).getRating();
        doReturn(10).when(anotherReview).getMaxRating();

        assertEquals(-1, new RatingComparator().compare(aSerieReview, anotherSerieReview));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void compareReviewsSame() {
        doReturn(5.5f).when(aReview).getRating();
        doReturn(20).when(aReview).getMaxRating();

        doReturn(5.5f).when(anotherReview).getRating();
        doReturn(20).when(anotherReview).getMaxRating();

        assertEquals(0, new RatingComparator().compare(aSerieReview, anotherSerieReview));
    }
}