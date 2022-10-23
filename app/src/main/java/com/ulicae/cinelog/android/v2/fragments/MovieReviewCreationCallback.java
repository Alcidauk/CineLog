package com.ulicae.cinelog.android.v2.fragments;

import android.view.View;

import com.ulicae.cinelog.data.dto.KinoDto;

public interface MovieReviewCreationCallback {

    void call(View view, KinoDto kinoDto);
}
