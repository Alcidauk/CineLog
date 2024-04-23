package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.dto.ItemDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface AsyncDataService<T extends ItemDto> {

    Completable createOrUpdate(T dtoObject);

    void delete(T dtoObject);

    Flowable<List<T>> findAll();

}
