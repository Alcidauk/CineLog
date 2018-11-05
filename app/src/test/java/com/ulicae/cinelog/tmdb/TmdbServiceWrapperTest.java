package com.ulicae.cinelog.tmdb;

import android.content.Context;

import com.ulicae.cinelog.PreferencesWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.services.SearchService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TmdbServiceWrapperTest {

    @Mock
    private Tmdb tmdb;

    @Mock
    private SearchService searchService;

    @Mock
    private Call call;

    @Mock
    private PreferencesWrapper preferencesWrapper;

    @Mock
    private Context context;

    @Test
    public void search() throws Exception {
        doReturn(searchService).when(tmdb).searchService();

        doReturn("fr").when(preferencesWrapper).getStringPreference(context, "default_tmdb_language", "en");

        doReturn(call).when(searchService).movie("name", 1, "fr", null, null, null, "ngram");

        assertEquals(
                call,
                new TmdbServiceWrapper(tmdb, context, preferencesWrapper).search("name")
        );
    }
}