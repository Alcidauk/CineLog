package com.ulicae.cinelog.network.task;

import android.os.AsyncTask;

import com.ulicae.cinelog.android.activities.add.AddReviewActivity;
import com.uwetrottmann.tmdb2.entities.BaseRatingObject;
import com.uwetrottmann.tmdb2.entities.BaseResultsPage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * CineLog Copyright 2018 Pierre Rognon
 * kinolog Copyright (C) 2017  ryan rigby
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
public class NetworkTaskManager<T extends BaseRatingObject> {

    private AddReviewActivity<T> addReviewActivity;
    private NetworkTaskCreator<AsyncTask, T> networkTaskCreator;
    private List<AsyncTask> taskList;

    public NetworkTaskManager(AddReviewActivity<T> addReviewActivity, NetworkTaskCreator<AsyncTask, T> networkTaskCreator) {
        this.addReviewActivity = addReviewActivity;
        this.networkTaskCreator = networkTaskCreator;
        this.taskList = new ArrayList<>();
    }

    public void createAndExecute(Call... call) {
        cancelTasks();

        AsyncTask networkTask = networkTaskCreator.create(addReviewActivity);

        //noinspection unchecked
        networkTask.execute(call);
        taskList.add(networkTask);
    }

    void cancelTasks() {
        for (AsyncTask task : taskList) {
            task.cancel(true);
        }

        taskList.clear();
    }

    public List<AsyncTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AsyncTask> taskList) {
        this.taskList = taskList;
    }
}
