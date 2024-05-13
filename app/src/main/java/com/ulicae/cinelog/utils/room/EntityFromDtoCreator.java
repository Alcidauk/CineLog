package com.ulicae.cinelog.utils.room;

import android.util.Log;

import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.room.dao.RoomDao;

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
public abstract class EntityFromDtoCreator<T, U extends RoomDao, D extends ItemDto> {

    private final U dao;

    public EntityFromDtoCreator(U dao) {
        this.dao = dao;
    }

    public void insertAll(List<D> items) {
        List<T> entities = new ArrayList<>();

        for (D item : items) {
            Log.i("room_migration", String.format("Preparing %s %s for the new room DB", item.getClass(), item.getId()));
            T instance = createRoomInstanceFromDto(item);
            if (instance != null) {
                entities.add(instance);
                Log.i("room_migration", String.format("Entity %s %s successfully prepared for the new room DB", item.getClass(), item.getId()));
            }
        }

        insertRoomEntities(entities);
    }

    private void insertRoomEntities(List<T> entities) {
        if (entities.isEmpty()) {
            return;
        }

        Class<? extends T> clazz = (Class<? extends T>) entities.get(0).getClass();
        Log.i("room_migration", String.format("Inserting %s %s entities in the new room DB", entities.size(), clazz));
        // TODO unique constraint fail, trouver pourquoi deux tmdb series.
        dao.insertAll(entities).subscribe();
        Log.i("room_migration", String.format("Entities %s %s inserted in the new room DB", entities.size(), clazz));
    }

    abstract T createRoomInstanceFromDto(D itemDto);
}
