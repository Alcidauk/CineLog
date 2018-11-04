package com.ulicae.cinelog.tmdb;

import com.ulicae.cinelog.AddKino;
import com.ulicae.cinelog.NetworkTask;

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