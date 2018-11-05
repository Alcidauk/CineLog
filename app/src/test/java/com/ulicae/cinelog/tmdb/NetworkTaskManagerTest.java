package com.ulicae.cinelog.tmdb;

import com.ulicae.cinelog.AddKino;
import com.ulicae.cinelog.NetworkTask;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
public class NetworkTaskManagerTest {

    @Mock
    private NetworkTaskCreator networkTaskCreator;

    @Mock
    private AddKino addKino;

    @Mock
    private Call<MovieResultsPage> call;

    @Mock
    private NetworkTask networkTask;

    @Test
    public void createAndExecute() throws Exception {
        doReturn(networkTask).when(networkTaskCreator).create(addKino);

        NetworkTaskManager networkTaskManager = new NetworkTaskManager(addKino, networkTaskCreator);
        networkTaskManager.createAndExecute(call);

        //noinspection unchecked
        verify(networkTask).execute(call);
        assertEquals(
                new ArrayList<NetworkTask>() {{
                    add(networkTask);
                }},
                networkTaskManager.getTaskList()
        );
    }

    @Test
    public void cancelTasks() throws Exception {
        NetworkTask anotherNetworkTask = mock(NetworkTask.class);

        NetworkTaskManager networkTaskManager = new NetworkTaskManager(addKino, networkTaskCreator);
        networkTaskManager.setTaskList(Arrays.asList(networkTask, anotherNetworkTask));

        networkTaskManager.cancelTasks();

        verify(networkTask).cancel(true);
        verify(anotherNetworkTask).cancel(true);
        // TODO assertEquals(emptyList(), networkTaskManager.getTaskList());
    }
}