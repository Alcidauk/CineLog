package com.alcidauk.cinelog.tmdb;

import com.alcidauk.cinelog.AddKino;
import com.alcidauk.cinelog.NetworkTask;


class NetworkTaskCreator {
    public NetworkTask create(AddKino addKino) {
        return new NetworkTask(addKino);
    }
}
