package com.ulicae.cinelog.room.services;

import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.dao.ReviewTagCrossRefDao;
import com.ulicae.cinelog.room.dao.TagDao;
import com.ulicae.cinelog.room.dto.utils.to.TagToDtoBuilder;
import com.ulicae.cinelog.room.entities.ReviewTagCrossRef;
import com.ulicae.cinelog.room.entities.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class ReviewTagAsyncService {

    private final ReviewTagCrossRefDao crossRefDao;
    private final TagDao tagDao;
    private final TagToDtoBuilder tagToDtoBuilder;

    public ReviewTagAsyncService(ReviewTagCrossRefDao crossRefDao, TagDao tagDao,
                                 TagToDtoBuilder tagToDtoBuilder) {
        this.crossRefDao = crossRefDao;
        this.tagDao = tagDao;
        this.tagToDtoBuilder = tagToDtoBuilder;
    }

    // TODO réfléchir au passage en async. A travailler parce que les calls remontent jusqu'à l'UI
    //  ce qui peut donc impacter l'affichage directement
    public List<TagDto> getReviewTags(KinoDto kinoDto) {
        List<ReviewTagCrossRef> crossRefs = crossRefDao
                .findForReview(kinoDto.getId())
                .blockingFirst();

        List<TagDto> tagDtos = new ArrayList<>();
        for (ReviewTagCrossRef crossRef : crossRefs) {
            TagDto tagDto = tagDao.find(crossRef.tagId)
                    .map(this::getTagDtoFromTag)
                    .blockingFirst();

            tagDtos.add(tagDto);
        }

        return tagDtos;
    }

    public Completable deleteForReview(Long reviewId) {
        return crossRefDao.deleteByReviewId(reviewId);
    }

    private TagDto getTagDtoFromTag(Tag item) {
        return tagToDtoBuilder.build(item);
    }



}
