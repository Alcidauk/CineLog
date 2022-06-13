package com.ulicae.cinelog.data.services.tags;

import com.ulicae.cinelog.data.AbstractJoinWithTagRepository;
import com.ulicae.cinelog.data.JoinLocalKinoWithTagRepository;
import com.ulicae.cinelog.data.JoinReviewWithTagRepository;
import com.ulicae.cinelog.data.TagRepository;
import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.JoinLocalKinoWithTag;
import com.ulicae.cinelog.data.dao.JoinReviewWithTag;
import com.ulicae.cinelog.data.dao.JoinWithTag;
import com.ulicae.cinelog.data.dao.SerieReview;
import com.ulicae.cinelog.data.dao.Tag;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.TagDto;
import com.ulicae.cinelog.data.dto.TagDtoBuilder;
import com.ulicae.cinelog.data.services.reviews.ItemService;
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
public class TagService implements ItemService<TagDto> {

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

    @Override
    public void createOrUpdateFromImport(List<TagDto> tagDtos) {
        for (TagDto tagDto : tagDtos) {
            createOrUpdate(tagDto);
        }
    }

    public List<TagDto> getMovieTags() {
        return buildTags(tagRepository.findMovieTags());
    }

    public List<TagDto> getSeriesTags() {
        return buildTags(tagRepository.findSeriesTags());
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
                tagDto.getId(),
                kinoDto instanceof SerieDto ?
                        ((SerieDto) kinoDto).getReviewId() : kinoDto.getKinoId()
        );

    }

    public void removeTagFromItemIfExists(TagDto tagDto, KinoDto kinoDto) {
        removeJoinWithTag(
                kinoDto instanceof SerieDto ?
                        joinReviewWithTagRepository : joinLocalKinoWithTagRepository,
                tagDto.getId(),
                kinoDto instanceof SerieDto ?
                        ((SerieDto) kinoDto).getReviewId() : kinoDto.getKinoId()
        );
    }

    private void addJoinWithTag(
            @SuppressWarnings("rawtypes") AbstractJoinWithTagRepository repository,
            Long tagId,
            Long entityId) {
        JoinWithTag existingJoin = repository.findByTagAndEntityId(tagId, entityId);
        if (existingJoin != null) {
            return;
        }

        repository.createJoin(tagId, entityId);
    }

    private void removeJoinWithTag(
            @SuppressWarnings("rawtypes") AbstractJoinWithTagRepository repository,
            Long tagId,
            Long entityId) {
        JoinWithTag existingJoin = repository.findByTagAndEntityId(tagId, entityId);
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

    public void removeTag(TagDto dataDto) {
        List<JoinLocalKinoWithTag> joinKino =
                joinLocalKinoWithTagRepository.findAllByTag(dataDto.getId());
        for (JoinLocalKinoWithTag joinLocalKinoWithTag : joinKino) {
            joinLocalKinoWithTagRepository.delete(joinLocalKinoWithTag.getId());
        }

        List<JoinReviewWithTag> joinReview =
                joinReviewWithTagRepository.findAllByTag(dataDto.getId());
        for (JoinReviewWithTag joinReviewWithTag : joinReview) {
            joinReviewWithTagRepository.delete(joinReviewWithTag.getId());
        }

        tagRepository.delete(dataDto.getId());
    }
}
