package com.ulicae.cinelog.android.v2.fragments;

import com.ulicae.cinelog.data.dto.KinoDto;

public interface MovieDetailsCallback {

    void call(KinoDto kinoDto, int position, boolean inDb);
}
