package com.ulicae.cinelog.data;

import android.content.Context;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.services.reviews.DataService;
import com.ulicae.cinelog.data.services.reviews.KinoService;
import com.ulicae.cinelog.data.services.reviews.SerieService;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
public class ServiceFactory {

    private Context context;

    public ServiceFactory(Context baseContext) {
        context = baseContext;
    }

    public DataService create(String type, DaoSession daoSession) {
        switch (type){
            case "kino":
                return new KinoService(daoSession);
            case "serie":
                return new SerieService(daoSession, context);
        }

        throw new NullPointerException("Unable to find a service for this type.");
    }
}
