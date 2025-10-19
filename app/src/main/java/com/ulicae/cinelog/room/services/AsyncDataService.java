package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.room.dto.ItemDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface AsyncDataService<T extends ItemDto> {

    Single createOrUpdate(T dtoObject);

    Single createOrUpdate(List<T> dtos);

    Completable delete(T dtoObject);

    Flowable<List<T>> findAll();

    Flowable<T> findById(Long id);

}
