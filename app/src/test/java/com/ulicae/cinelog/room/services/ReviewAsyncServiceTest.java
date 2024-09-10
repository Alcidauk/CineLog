package com.ulicae.cinelog.room.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.ulicae.cinelog.data.ReviewRepository;
import com.ulicae.cinelog.data.SerieReviewRepository;
import com.ulicae.cinelog.data.TmdbSerieRepository;
import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.data.dto.SerieKinoDtoBuilder;
import com.ulicae.cinelog.room.CinelogSchedulers;
import com.ulicae.cinelog.room.dao.ReviewAsyncDao;
import com.ulicae.cinelog.room.dto.utils.from.ReviewFromDtoCreator;
import com.ulicae.cinelog.room.dto.utils.to.ReviewToDataDtoBuilder;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Review;
import com.ulicae.cinelog.utils.SerieDtoToDbBuilder;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.rxjava3.core.Single;

@RunWith(MockitoJUnitRunner.class)
public class ReviewAsyncServiceTest extends TestCase {

    @Mock
    private ReviewAsyncDao reviewAsyncDao;

    @Mock
    private ReviewFromDtoCreator reviewFromDtoCreator;

    @Mock
    private ReviewToDataDtoBuilder reviewToDataDtoBuilder;

    @Mock
    private CinelogSchedulers cinelogSchedulers;

    @Mock
    private SerieReviewRepository serieReviewRepository;

    @Mock
    private TmdbSerieRepository tmdbSerieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private SerieKinoDtoBuilder serieKinoDtoBuilder;

    @Mock
    private SerieDto serieDto;

    @Mock
    private SerieDtoToDbBuilder toDbBuilder;




    @Test
    public void getByTmdbMovieId() {
        Review review = mock(Review.class);

        doReturn(serieDto).when(reviewToDataDtoBuilder).build(review);

        Single single = Single.just(review);
        doReturn(single).when(reviewAsyncDao).findByMovieIdSingle(45L);

        Single<KinoDto> withTmdbId = new ReviewAsyncService(
                reviewAsyncDao,
                null,
                null,
                reviewToDataDtoBuilder,
                cinelogSchedulers,
                ItemEntityType.MOVIE
        ).getWithTmdbId(45L);

        withTmdbId.test().assertValue(serieDto);
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
    public void getByRatingAsc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAllByRating(true);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByRating(true)
        );
    }

    @Test
    public void getByRatingDesc() {
        final SerieReview serieReview = mock(SerieReview.class);

        doReturn(new ArrayList<SerieReview>() {{
            add(serieReview);
        }}).when(serieReviewRepository).findAllByRating(false);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);

        assertEquals(
                new ArrayList<SerieDto>() {{
                    add(serieDto);
                }},
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByRating(false)
        );
    }

    @Test
    public void getAllByTitleAsc() {
        SerieReview serieReview = mock(SerieReview.class);
        SerieReview anotherSerieReview = mock(SerieReview.class);

        doReturn(Arrays.asList(serieReview, anotherSerieReview)).when(serieReviewRepository).findAllByTitle(true);

        SerieDto anotherSerieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);
        doReturn(anotherSerieDto).when(serieKinoDtoBuilder).build(anotherSerieReview);

        assertEquals(
                Arrays.asList(serieDto, anotherSerieDto),
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByTitle(true)
        );
    }

    @Test
    public void getAllByTitleDesc() {
        SerieReview serieReview = mock(SerieReview.class);
        SerieReview anotherSerieReview = mock(SerieReview.class);

        doReturn(Arrays.asList(serieReview, anotherSerieReview)).when(serieReviewRepository).findAllByTitle(false);

        SerieDto anotherSerieDto = mock(SerieDto.class);
        doReturn(serieDto).when(serieKinoDtoBuilder).build(serieReview);
        doReturn(anotherSerieDto).when(serieKinoDtoBuilder).build(anotherSerieReview);

        assertEquals(
                Arrays.asList(serieDto, anotherSerieDto),
                new SerieService(serieReviewRepository, reviewRepository, tmdbSerieRepository, tmdbGetterService, serieKinoDtoBuilder, toDbBuilder, null).getAllByTitle(false)
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