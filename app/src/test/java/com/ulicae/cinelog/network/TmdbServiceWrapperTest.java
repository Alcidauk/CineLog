package com.ulicae.cinelog.network;

import android.content.Context;

import com.ulicae.cinelog.utils.PreferencesWrapper;
import com.ulicae.cinelog.utils.LocaleWrapper;
import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.services.SearchService;
import com.uwetrottmann.tmdb2.services.TvService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * <p>
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
@RunWith(MockitoJUnitRunner.class)
public class TmdbServiceWrapperTest {

    @Mock
    private Tmdb tmdb;

    @Mock
    private SearchService searchService;

    @Mock
    private TvService tvService;

    @Mock
    private Call call;

    @Mock
    private PreferencesWrapper preferencesWrapper;

    @Mock
    private LocaleWrapper localeWrapper;

    @Mock
    private Context context;

    @Test
    public void searchMovie() throws Exception {
        doReturn(searchService).when(tmdb).searchService();

        doReturn("fr").when(preferencesWrapper).getStringPreference(context, "default_tmdb_language", null);

        doReturn(call).when(searchService).movie("name", 1, "fr", null, null, null, null);

        assertEquals(
                call,
                new TmdbServiceWrapper(tmdb, context, preferencesWrapper, localeWrapper).search("name")
        );
    }

    @Test
    public void searchTv() throws Exception {
        doReturn(searchService).when(tmdb).searchService();

        doReturn("fr").when(preferencesWrapper).getStringPreference(context, "default_tmdb_language", null);

        doReturn(call).when(searchService).tv("name", 1, "fr", null);

        assertEquals(
                call,
                new TmdbServiceWrapper(tmdb, context, preferencesWrapper, localeWrapper).searchTv("name")
        );
    }

    @Test
    public void searchTvShowById() {
        doReturn(tvService).when(tmdb).tvService();

        doReturn("fr").when(preferencesWrapper).getStringPreference(context, "default_tmdb_language", null);

        doReturn(call).when(tvService).tv(15, "fr");

        assertEquals(
                call,
                new TmdbServiceWrapper(tmdb, context, preferencesWrapper, localeWrapper).searchTvShowById(15)
        );
    }

    @Test
    public void initSearchLanguage() throws Exception {
        doReturn(null).when(preferencesWrapper).getStringPreference(context, "default_tmdb_language", null);

        LocaleWrapper localeWrapper = mock(LocaleWrapper.class);
        doReturn("banane").when(localeWrapper).getLanguage();

        new TmdbServiceWrapper(tmdb, context, preferencesWrapper, localeWrapper).initSearchLanguageIfNeeded();

        verify(preferencesWrapper).setStringPreference(context, "default_tmdb_language", "banane");
    }
}