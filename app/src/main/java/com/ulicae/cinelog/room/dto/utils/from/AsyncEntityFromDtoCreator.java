package com.ulicae.cinelog.room.dto.utils.from;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ulicae.cinelog.data.dto.ItemDto;
import com.ulicae.cinelog.room.dao.AsyncRoomDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

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
public abstract class AsyncEntityFromDtoCreator<T, U extends AsyncRoomDao, D extends ItemDto> {

    private final U dao;

    public AsyncEntityFromDtoCreator(U dao) {
        this.dao = dao;
    }

    public Completable insertAll(List<D> items) {
        return dao.insertAll(buildEntities(items));
    }

    public void insertAllForMigration(List<D> items) {
        List<T> entities = buildEntities(items);

        insertRoomEntities(entities);
    }

    @NonNull
    private List<T> buildEntities(List<D> items) {
        List<T> entities = new ArrayList<>();

        for (D item : items) {
            T instance = createRoomInstanceFromDto(item);
            if (instance != null) {
                entities.add(instance);
            }
        }
        return entities;
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

    public Completable insert(D itemDto) {
        return dao.insert(createRoomInstanceFromDto(itemDto));
    }
}
