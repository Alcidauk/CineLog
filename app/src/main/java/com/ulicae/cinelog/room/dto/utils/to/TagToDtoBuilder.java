package com.ulicae.cinelog.room.dto.utils.to;

import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.entities.Tag;

/**
 * CineLog Copyright 2024 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class TagToDtoBuilder {

    public TagDto build(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getColor(),
                tag.isForMovies(),
                tag.isForSeries()
        );
    }
}
