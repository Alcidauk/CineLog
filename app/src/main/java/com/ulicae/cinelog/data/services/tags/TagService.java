package com.ulicae.cinelog.data.services.tags;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dto.TagDto;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class TagService {

   // private final TagRepository tagRepository;
    // private final KinoDtoBuilder kinoDtoBuilder;
    // private KinoDtoToDbBuilder kinoDtoToDbBuilder;

    public TagService(DaoSession session) {
        //this.tagRepository = new TagRepository(session);
    }

   /* TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }*/

    public List<TagDto> getAll() {
        // TODO retrieve tags in DB
        //List<LocalKino> tags = localKinoRepository.findAll();

        ArrayList<TagDto> tagDtos = new ArrayList<>();
        tagDtos.add(new TagDto(13L, "Horreur", "#000000"));
        tagDtos.add(new TagDto(13L, "Malheur", "#a34567"));
        return tagDtos;
    }

}
