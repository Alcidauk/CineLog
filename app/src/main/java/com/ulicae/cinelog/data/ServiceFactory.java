package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;

public class ServiceFactory {
    public DataService create(String type, DaoSession daoSession) {
        switch (type){
            case "kino":
                return new KinoService(daoSession);
            case "serie":
                return new SerieService(daoSession);
        }

        throw new NullPointerException("Unable to find a service for this type.");
    }
}
