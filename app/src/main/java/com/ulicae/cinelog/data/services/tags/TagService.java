package com.ulicae.cinelog.data.services.tags;

import com.ulicae.cinelog.data.AbstractJoinWithTagRepository;
import com.ulicae.cinelog.data.JoinLocalKinoWithTagRepository;
import com.ulicae.cinelog.data.JoinReviewWithTagRepository;
import com.ulicae.cinelog.data.TagRepository;
import com.ulicae.cinelog.data.dao.JoinWithTag;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.TagDtoBuilder;
import com.ulicae.cinelog.utils.TagDtoToDbBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * CineLog Copyright 2022 Pierre Rognon
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

    private final TagRepository tagRepository;
    private final JoinLocalKinoWithTagRepository joinLocalKinoWithTagRepository;
    private final JoinReviewWithTagRepository joinReviewWithTagRepository;

    private final TagDtoBuilder tagDtoBuilder;
    private final TagDtoToDbBuilder tagDtoToDbBuilder;

    public TagService(DaoSession session) {
        this(
                new TagRepository(session),
                new JoinLocalKinoWithTagRepository(session),
                new JoinReviewWithTagRepository(session),
                new TagDtoBuilder(),
                new TagDtoToDbBuilder()
        );
    }

    public TagService(TagRepository tagRepository,
                      JoinLocalKinoWithTagRepository joinLocalKinoWithTagRepository,
                      JoinReviewWithTagRepository joinReviewWithTagRepository,
                      TagDtoBuilder tagDtoBuilder,
                      TagDtoToDbBuilder tagDtoToDbBuilder) {
        this.tagRepository = tagRepository;
        this.joinLocalKinoWithTagRepository = joinLocalKinoWithTagRepository;
        this.joinReviewWithTagRepository = joinReviewWithTagRepository;
        this.tagDtoBuilder = tagDtoBuilder;
        this.tagDtoToDbBuilder = tagDtoToDbBuilder;
    }


    public List<TagDto> getAll() {
        return buildTags(tagRepository.findAll());
    }

    public void createOrUpdate(TagDto tagDto) {
        Tag tag = tagDtoToDbBuilder.build(tagDto);

        if (tag != null) {
            tagRepository.createOrUpdate(tag);
        }
    }

    public void addTagToItemIfNotExists(TagDto tagDto, KinoDto kinoDto) {
        addJoinWithTag(
                kinoDto instanceof SerieDto ?
                        joinReviewWithTagRepository : joinLocalKinoWithTagRepository,
                tagDto,
                kinoDto
        );

    }

    public void removeTagFromItemIfExists(TagDto tagDto, KinoDto kinoDto) {
        removeJoinWithTag(
                kinoDto instanceof SerieDto ?
                        joinReviewWithTagRepository : joinLocalKinoWithTagRepository,
                tagDto,
                kinoDto
        );
    }

    private void addJoinWithTag(
            @SuppressWarnings("rawtypes") AbstractJoinWithTagRepository repository,
            TagDto tagDto,
            KinoDto kinoDto) {
        JoinWithTag existingJoin =
                repository.findByTagAndKinoId(tagDto.getId(), kinoDto.getKinoId());
        if (existingJoin != null) {
            return;
        }

        repository.createJoin(tagDto.getId(), kinoDto.getKinoId());
    }

    private void removeJoinWithTag(
            @SuppressWarnings("rawtypes") AbstractJoinWithTagRepository repository,
            TagDto tagDto,
            KinoDto kinoDto) {
        JoinWithTag existingJoin =
                repository.findByTagAndKinoId(tagDto.getId(), kinoDto.getKinoId());

        if (existingJoin != null) {
            repository.delete(existingJoin.getId());
        }
    }


    private List<TagDto> buildTags(List<Tag> tags) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            tagDtos.add(tagDtoBuilder.build(tag));
        }

        return tagDtos;
    }

}
