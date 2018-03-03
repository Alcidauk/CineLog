package com.alcidauk.cinelog.addkino;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KinoCreatorTest {

    @Mock
    private LocalKinoRepository localKinoRepository;

    @Test
    public void createKino() throws Exception {
        LocalKino kinoToCreate = new LocalKino(
                "La belle aux bois dormants",
                0f,
                null,
                null,
                0,
                null,
                null,
                0,
                null
        );
        assertEquals(
                kinoToCreate,
                new KinoCreator(localKinoRepository).create("La belle aux bois dormants")
        );

        verify(localKinoRepository).create(kinoToCreate);
    }
}