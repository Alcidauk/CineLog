package com.ulicae.cinelog.tmdb;

import com.ulicae.cinelog.AddKino;
import com.ulicae.cinelog.NetworkTask;


class NetworkTaskCreator {
    public NetworkTask create(AddKino addKino) {
        return new NetworkTask(addKino);
    }
}
