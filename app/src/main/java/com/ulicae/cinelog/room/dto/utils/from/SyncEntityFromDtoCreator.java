package com.ulicae.cinelog.room.dto.utils.from;

import android.util.Log;

import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.room.dao.SyncRoomDao;

import java.util.ArrayList;
import java.util.List;

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
public abstract class SyncEntityFromDtoCreator<T, U extends SyncRoomDao, D extends ItemDto> {

    private final U dao;

    public SyncEntityFromDtoCreator(U dao) {
        this.dao = dao;
    }

    public Long[] insertAll(List<D> items) {
        List<Long> ids = new ArrayList<>();

        for (D item : items) {
            ids.add(this.insert(item));
        }

        return ids.toArray(new Long[0]);
    }

    public long insert(D item) {
        T instance = createRoomInstanceFromDto(item);
        if (instance != null) {
            Log.i("room_migration", String.format("Preparing %s %s for the new room DB", item.getClass(), item.getId()));
            return dao.insert(instance);
        }

        // TODO throw exception ?
        return 0;
    }

    abstract T createRoomInstanceFromDto(D itemDto);
}
