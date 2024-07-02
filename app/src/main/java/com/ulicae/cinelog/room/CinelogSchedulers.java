package com.ulicae.cinelog.room;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CinelogSchedulers {

    public Scheduler io() {
        return Schedulers.io();
    }

    public Scheduler androidMainThread() {
        return AndroidSchedulers.mainThread();
    }
}
