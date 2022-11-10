package com.ulicae.cinelog.android.v2.fragments.review.add;

import com.ulicae.cinelog.data.dto.KinoDto;

public interface ReviewItemCallback {

    void call(KinoDto kinoDto, int position, boolean inDb);
}
