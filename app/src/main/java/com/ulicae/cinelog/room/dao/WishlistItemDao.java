package com.ulicae.cinelog.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.WishlistItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

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
@Dao
public interface WishlistItemDao extends RoomDao<WishlistItem> {


    @Query("SELECT * FROM wishlistitem WHERE item_entity_type = :itemEntityType")
    Flowable<List<WishlistItem>> findAll(ItemEntityType itemEntityType);

    @Query("SELECT * FROM wishlistitem WHERE id = :id")
    Flowable<WishlistItem> find(long id);

    // TODO create is not update
    @Insert
    Completable createOrUpdate(WishlistItem wishlistItem);

}
