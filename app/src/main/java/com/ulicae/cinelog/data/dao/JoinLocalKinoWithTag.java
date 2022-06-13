package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.parceler.Parcel;
import org.greenrobot.greendao.annotation.Generated;

/**
 * CineLog Copyright 2022 Pierre Rognon
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
@Parcel
@Entity
public class JoinLocalKinoWithTag implements JoinWithTag {
    @Id
    private Long id;

    private Long tagId;

    private Long localKinoId;

    public JoinLocalKinoWithTag(Long tagId, Long localKinoId) {
        this.tagId = tagId;
        this.localKinoId = localKinoId;
    }


    @Generated(hash = 1004253312)
    public JoinLocalKinoWithTag(Long id, Long tagId, Long localKinoId) {
        this.id = id;
        this.tagId = tagId;
        this.localKinoId = localKinoId;
    }


    @Generated(hash = 1285849593)
    public JoinLocalKinoWithTag() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getLocalKinoId() {
        return this.localKinoId;
    }

    public void setLocalKinoId(Long localKinoId) {
        this.localKinoId = localKinoId;
    }
}

