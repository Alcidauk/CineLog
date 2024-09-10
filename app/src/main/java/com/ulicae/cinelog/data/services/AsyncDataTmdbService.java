package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.dto.ItemDto;

import io.reactivex.rxjava3.core.Single;

public interface AsyncDataTmdbService<T extends ItemDto> extends AsyncDataService<T> {

    Single<T> getWithTmdbId(long tmdbId);

}
