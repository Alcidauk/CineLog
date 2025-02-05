package com.ulicae.cinelog.room.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.ReviewAsyncDao;
import com.ulicae.cinelog.room.dto.KinoDto;
import com.ulicae.cinelog.room.dto.SerieDto;
import com.ulicae.cinelog.room.dto.TagDto;
import com.ulicae.cinelog.room.dto.utils.from.ReviewFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.to.ReviewToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class ReviewAsyncServiceTest extends TestCase {

    @Mock
    private ReviewAsyncDao reviewAsyncDao;
    @Mock
    private ReviewTagAsyncService reviewTagAsyncService;
    @Mock
    private SerieEpisodeAsyncService serieEpisodeAsyncService;

    @Mock
    private ReviewToDataDtoBuilder reviewToDataDtoBuilder;

    @Mock
    private CinelogSchedulers cinelogSchedulers;

    @Mock
    private ReviewFromDtoCreator reviewFromDtoCreator;


    @Mock
    private SerieDto serieDto;
    @Mock
    private KinoDto kinoDto;

    @Test
    public void getByTmdbMovieId() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Single single = Single.just(review);
        doReturn(single).when(reviewAsyncDao).findByMovieIdSingle(45L);

        Single<KinoDto> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.MOVIE
        ).getWithTmdbId(45L);

        withTmdbId.test().assertValue(serieDto);
    }


    @Test
    public void testFindAll() {
        Review review = mock(Review.class);

        doReturn(kinoDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAll();

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                null
        ).findAll();

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(kinoDto);
        }});
    }

    @Test
    public void testFindAllMovies() {
        Review review = mock(Review.class);

        doReturn(kinoDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAll(ItemEntityType.MOVIE);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.MOVIE
        ).findAll();

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(kinoDto);
        }});
    }

    @Test
    public void testFindAllSeriesWithoutTags() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAll(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findAll();

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindAllSeriesWithTags() {
        Review review = mock(Review.class);
        TagDto tagDto = mock(TagDto.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAll(ItemEntityType.SERIE);

        doReturn(new ArrayList() {{
            add(tagDto);
        }}).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findAll();

        TestSubscriber<List<KinoDto>> subscriber = withTmdbId.test();

        verify(serieDto).setTags(new ArrayList<TagDto>() {{
            add(tagDto);
        }});
        subscriber.assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }


    @Test
    public void testFindByRatingAsc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByRatingAsc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByRating(true);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindByRatingDesc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByRatingDesc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByRating(false);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindByTitleAsc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByTitleAsc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByTitle(true);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindByTitleDesc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByTitleDesc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByTitle(false);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }


    @Test
    public void testFindByReviewDateAsc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByReviewDateAsc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByReviewDate(true);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }


    @Test
    public void testFindByReviewDateDesc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByReviewDateDesc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByReviewDate(false);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindByYearAsc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByYearAsc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByYear(true);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }

    @Test
    public void testFindByYearDesc() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Flowable flowable = Flowable.just(new ArrayList<Review>() {{
            add(review);
        }});
        doReturn(flowable).when(reviewAsyncDao).findAllByYearDesc(ItemEntityType.SERIE);

        doReturn(new ArrayList()).when(reviewTagAsyncService).getReviewTags(serieDto);

        Flowable<List<KinoDto>> withTmdbId = new ReviewAsyncService(
                reviewTagAsyncService,
                serieEpisodeAsyncService,
                reviewFromDtoCreator,
                reviewAsyncDao,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.SERIE
        ).findByYear(false);

        withTmdbId.test().assertValue(new ArrayList<KinoDto>() {{
            add(serieDto);
        }});
    }


/*
    @Test
    public void getReview() {
        SerieReview serieReview = mock(SerieReview.class);

        doReturn(serieReview).when(serieReviewRepository).find(4L);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                serieDto,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getReview(4L)
        );
    }

    @Test
    public void getAll() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAll();
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAll()
        );
    }

    @Test
    public void delete() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(545L).when(serieDto).getKinoId();

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).delete(serieDto);

        verify(serieReviewRepository).delete(545L);
    }

    @Test
    public void createOrUpdateKino() {
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(12L).when(serieDto).getKinoId();

        SerieReview serieReview = new SerieReview(12L, tmdbKino, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateSerieNullTmdbId() {
        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(45L).when(serieDto).getKinoId();

        SerieReview reviewToCreate = new SerieReview(45L, null, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(reviewToCreate);

        assertEquals(
                createdKino,
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).createOrUpdate(serieDto)
        );

        verify(serieReviewRepository).createOrUpdate(reviewToCreate);
        verify(reviewRepository).createOrUpdate(review);
        verify(tmdbSerieRepository, never()).createOrUpdate(any(TmdbSerie.class));
    }

    @Test
    public void createOrUpdateKinosNoExistingId() {
        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        //noinspection ResultOfMethodCallIgnored
        doReturn(12L).when(serieDto).getKinoId();

        SerieReview serieReview = new SerieReview(12L, tmdbKino, review);

        SerieDto createdKino = mock(SerieDto.class);
        doReturn(createdKino).when(serieKinoDtoBuilder).build(serieReview);

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null)
                .createOrUpdateFromImport(new ArrayList<SerieDto>() {{
                    add(serieDto);
                }});

        verify(serieDto, never()).setKinoId(12L);

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }

    @Test
    public void createOrUpdateKinosWithTmdbIdExistingWithId() {
        //noinspection ResultOfMethodCallIgnored
        doReturn(null).when(serieDto).getKinoId();
        //noinspection ResultOfMethodCallIgnored
        doReturn(4561432L).when(serieDto).getTmdbKinoId();

        TmdbSerie tmdbKino = mock(TmdbSerie.class);
        doReturn(tmdbKino).when(toDbBuilder).buildTmdbSerie(serieDto);

        Review review = mock(Review.class);
        doReturn(review).when(toDbBuilder).buildReview(serieDto);

        SerieReview serieReview = new SerieReview(null, tmdbKino, review);
        SerieReview dbSerieReview = new SerieReview(24L, tmdbKino, review);

        doReturn(dbSerieReview).when(serieReviewRepository).findByMovieId(4561432L);

        new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null)
                .createOrUpdateFromImport(new ArrayList<SerieDto>() {{
                    add(serieDto);
                }});

        verify(serieDto).setKinoId(24L);

        verify(serieReviewRepository).createOrUpdate(serieReview);
        verify(tmdbSerieRepository).createOrUpdate(tmdbKino);
    }*/

}