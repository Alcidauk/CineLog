package com.ulicae.cinelog.tmdb;

import com.ulicae.cinelog.AddKino;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import retrofit2.Call;

import static java.util.Collections.emptyList;
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
public class NetworkTaskManagerTest {

    @Mock
    private NetworkTaskCreator networkTaskCreator;

    @Mock
    private AddKino addKino;

    @Mock
    private Call<MovieResultsPage> call;

    @Mock
    private MovieNetworkTask networkTask;

    @Test
    public void createAndExecute() throws Exception {
        doReturn(networkTask).when(networkTaskCreator).create(addKino);

        NetworkTaskManager networkTaskManager = new NetworkTaskManager(addKino, networkTaskCreator);
        networkTaskManager.createAndExecute(call);

        //noinspection unchecked
        verify(networkTask).execute(call);
        assertEquals(
                new ArrayList<MovieNetworkTask>() {{
                    add(networkTask);
                }},
                networkTaskManager.getTaskList()
        );
    }

    @Test
    public void createAndExecute_verifyEmptyTasks() throws Exception {
        doReturn(networkTask).when(networkTaskCreator).create(addKino);

        final MovieNetworkTask anotherNetworkTask = mock(MovieNetworkTask.class);

        NetworkTaskManager networkTaskManager = new NetworkTaskManager(addKino, networkTaskCreator);
        networkTaskManager.setTaskList(new ArrayList<MovieNetworkTask>() {{
            add(anotherNetworkTask);
        }});

        networkTaskManager.createAndExecute(call);

        //noinspection unchecked
        verify(networkTask).execute(call);
        assertEquals(
                new ArrayList<MovieNetworkTask>() {{
                    add(networkTask);
                }},
                networkTaskManager.getTaskList()
        );
    }

    @Test
    public void cancelTasks() throws Exception {
        final MovieNetworkTask anotherNetworkTask = mock(MovieNetworkTask.class);

        NetworkTaskManager networkTaskManager = new NetworkTaskManager(addKino, networkTaskCreator);
        networkTaskManager.setTaskList(
                new ArrayList<MovieNetworkTask>() {{
                    add(networkTask);
                    add(anotherNetworkTask);
                }}
        );

        networkTaskManager.cancelTasks();

        verify(networkTask).cancel(true);
        verify(anotherNetworkTask).cancel(true);

        assertEquals(emptyList(), networkTaskManager.getTaskList());
    }
}