package com.ulicae.cinelog.data.dto.data;

import com.ulicae.cinelog.data.dao.TmdbSerie;
import com.ulicae.cinelog.data.dao.WishlistSerie;

public class WishlistSerieToSerieDataDtoBuilder {

    public SerieDataDto build(WishlistSerie wishlistSerie) {
        TmdbSerie serie = wishlistSerie.getSerie();
        return new SerieDataDto(
                wishlistSerie.getWishlist_serie_id(),
                serie != null && serie.getSerie_id() != null ? serie.getSerie_id().intValue() : null,
                wishlistSerie.getTitle(),
                serie != null ? serie.getPoster_path() : null,
                serie != null ? serie.getOverview() : null,
                serie != null ? serie.getYear() : 0,
                serie != null ? serie.getRelease_date() : null
        );
    }
}
