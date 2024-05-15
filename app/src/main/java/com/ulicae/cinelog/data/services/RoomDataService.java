package com.ulicae.cinelog.data.services;

import com.ulicae.cinelog.data.dto.ItemDto;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface RoomDataService<T extends ItemDto> {

    void createOrUpdate(T dtoObject);

    void delete(T dtoObject);

    Flowable<List<T>> findAll();

}
