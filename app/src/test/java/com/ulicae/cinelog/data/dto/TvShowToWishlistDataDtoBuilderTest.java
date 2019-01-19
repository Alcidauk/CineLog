package com.ulicae.cinelog.data.dto;

import com.ulicae.cinelog.data.dto.data.WishlistDataDto;
import com.ulicae.cinelog.data.dto.data.TvShowToSerieDataDtoBuilder;
import com.ulicae.cinelog.data.dto.data.WishlistItemType;
import com.uwetrottmann.tmdb2.entities.TvShow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TvShowToWishlistDataDtoBuilderTest {

    @Test
    public void build() throws ParseException {
        TvShow tvShow = new TvShow();
        tvShow.id = 45;
        tvShow.name = "Versailles";
        tvShow.poster_path = "45454545";
        tvShow.overview = "A magic castle";
        tvShow.first_air_date = new SimpleDateFormat("dd/MM/yy").parse("01/10/2015");

        assertEquals(
                new WishlistDataDto(null, 45, "Versailles", "45454545", "A magic castle", 2015, "01/10/2015", WishlistItemType.SERIE),
                new TvShowToSerieDataDtoBuilder().build(tvShow)
        );
    }

    @Test
    public void build_nullDate() {
        TvShow tvShow = new TvShow();
        tvShow.id = 45;
        tvShow.name = "Versailles";
        tvShow.poster_path = "45454545";
        tvShow.overview = "A magic castle";
        tvShow.first_air_date = null;

        assertEquals(
                new WishlistDataDto(null,45, "Versailles", "45454545", "A magic castle", 0, null, WishlistItemType.SERIE),
                new TvShowToSerieDataDtoBuilder().build(tvShow)
        );
    }
}