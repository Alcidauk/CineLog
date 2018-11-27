package com.ulicae.cinelog.network;

import com.ulicae.cinelog.data.dto.KinoDto;

public interface DtoBuilderFromTmdbObject<T> {

    KinoDto build(T tmdbObject);

}
