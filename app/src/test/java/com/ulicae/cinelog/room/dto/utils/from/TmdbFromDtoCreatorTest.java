package com.ulicae.cinelog.room.dto.utils.from;

import com.ulicae.cinelog.data.dto.KinoDto;
import com.ulicae.cinelog.data.dto.SerieDto;
import com.ulicae.cinelog.room.dao.TmdbDao;
import com.ulicae.cinelog.room.dto.utils.from.TmdbFromDtoCreator;
import com.ulicae.cinelog.room.entities.ItemEntityType;
import com.ulicae.cinelog.room.entities.Tmdb;

import junit.framework.TestCase;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;

public class TmdbFromDtoCreatorTest extends TestCase {


    @Mock
    private TmdbDao tmdbDao;

    public void testCreateRoomInstanceFromMovieDto() {
        Date date = new Date();
        assertEquals(
                new Tmdb(
                        23,
                        ItemEntityType.MOVIE,
                        "/aposterpath.jpg",
                        "An overview",
                        2000,
                        "12/12/2024"
                ),
                new TmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new KinoDto(
                                        453L,
                                        23L,
                                        "a kino",
                                        date,
                                        "a kino review",
                                        5.0f,
                                        5,
                                        "/aposterpath.jpg",
                                        "An overview",
                                        2000,
                                        "12/12/2024",
                                        new ArrayList<>()
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromMovieDtoNoTmdb() {
        Date date = new Date();
        assertNull(
                new TmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new KinoDto(
                                        453L,
                                        0L,
                                        "a kino",
                                        date,
                                        "a kino review",
                                        5.0f,
                                        5,
                                        "/aposterpath.jpg",
                                        "An overview",
                                        2000,
                                        "12/12/2024",
                                        new ArrayList<>()
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDto() {
        Date date = new Date();
        assertEquals(
                new Tmdb(
                        23,
                        ItemEntityType.SERIE,
                        "/aposterpath.jpg",
                        "An overview",
                        2000,
                        "12/12/2024"
                ),
                new TmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new SerieDto(
                                        453L,
                                        23L,
                                        45L,
                                        "a kino",
                                        date,
                                        "a kino review",
                                        5.0f,
                                        5,
                                        "/aposterpath.jpg",
                                        "An overview",
                                        2000,
                                        "12/12/2024",
                                        new ArrayList<>()
                                )
                        )
        );
    }

    public void testCreateRoomInstanceFromSerieDtoNoTmdb() {
        Date date = new Date();
        assertNull(
                new TmdbFromDtoCreator(tmdbDao)
                        .createRoomInstanceFromDto(
                                new SerieDto(
                                        453L,
                                        0L,
                                        45L,
                                        "a kino",
                                        date,
                                        "a kino review",
                                        5.0f,
                                        5,
                                        "/aposterpath.jpg",
                                        "An overview",
                                        2000,
                                        "12/12/2024",
                                        new ArrayList<>()
                                )
                        )
        );
    }

}