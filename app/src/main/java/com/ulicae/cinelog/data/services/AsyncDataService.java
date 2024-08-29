package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.data.dto.data.WishlistDataDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface AsyncDataService<T extends ItemDto> {

    Completable createOrUpdate(T dtoObject);

    Completable createOrUpdate(List<T> dtos);

    Completable delete(T dtoObject);

    Flowable<List<T>> findAll();

    Flowable<T> findById(Long id);

}
