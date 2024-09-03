package com.ulicae.cinelog.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * CineLog Copyright 2024 Pierre Rognon
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
public class ToasterWrapper {

    public enum ToasterDuration {
        SHORT,
        LONG
    }

    private final Context context;

    public ToasterWrapper(Context context){
        this.context = context;
    }

    public void toast(int value, ToasterDuration duration) {
        Toast.makeText(context, value, getToastDuration(duration)).show();
    }

    public int getToastDuration(ToasterDuration toasterDuration) {
        switch (toasterDuration) {
            case LONG:
                return Toast.LENGTH_LONG;
            case SHORT:
                return Toast.LENGTH_SHORT;
            default:
                return Toast.LENGTH_SHORT;
        }
    }

}
