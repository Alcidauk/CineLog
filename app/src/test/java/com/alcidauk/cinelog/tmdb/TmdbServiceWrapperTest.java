package com.alcidauk.cinelog.tmdb;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.services.SearchService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TmdbServiceWrapperTest {

    @Mock
    private Tmdb tmdb;

    @Mock
    private SearchService searchService;

    @Mock
    private Call call;

    @Test
    public void search() throws Exception {
        doReturn(searchService).when(tmdb).searchService();

        doReturn(call).when(searchService).movie("name", 1, null, null, null, null, "ngram");

        assertEquals(
                call,
                new TmdbServiceWrapper(tmdb).search("name")
        );
    }
}