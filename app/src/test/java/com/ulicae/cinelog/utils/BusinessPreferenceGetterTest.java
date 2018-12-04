package com.ulicae.cinelog.utils;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class BusinessPreferenceGetterTest {

    @Mock
    private PreferencesWrapper preferencesWrapper;

    @Mock
    private Context context;

    @Test
    public void getAutomaticExport() {
        doReturn(true).when(preferencesWrapper).getBooleanPreference(context, "automatic_save", false);

        assertTrue(new BusinessPreferenceGetter(context, preferencesWrapper).getAutomaticExport());
    }
}