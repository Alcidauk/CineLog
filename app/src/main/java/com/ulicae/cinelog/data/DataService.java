package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dto.KinoDto;

public interface DataService<T extends KinoDto> {

    void delete(T dtoObject);

    T getWithTmdbId(long tmdbId);

    T createOrUpdate(T dtoObject);

}
