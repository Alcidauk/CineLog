package com.alcidauk.cinelog.tmdb;

import com.alcidauk.cinelog.AddKino;
import com.alcidauk.cinelog.NetworkTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NetworkTaskCreatorTest {

    @Mock
    private AddKino addKino;

    @Test
    public void create() throws Exception {
        assertEquals(
                new NetworkTask(addKino),
                new NetworkTaskCreator().create(addKino)
        );
    }
}