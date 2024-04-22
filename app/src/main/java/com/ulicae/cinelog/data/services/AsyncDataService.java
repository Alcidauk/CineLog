package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.dto.ItemDto;

import io.reactivex.Completable;

public interface AsyncDataService<T extends ItemDto> {

    Completable createOrUpdate(T dtoObject);

}
