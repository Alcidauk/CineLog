package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.room.dto.ItemDto;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface RoomDataService<T extends ItemDto> {

    void createOrUpdate(T dtoObject);

    void delete(T dtoObject);

    Flowable<List<T>> findAll();

}
