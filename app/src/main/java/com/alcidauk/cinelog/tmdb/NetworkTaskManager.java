package com.alcidauk.cinelog.tmdb;

import com.alcidauk.cinelog.AddKino;
import com.alcidauk.cinelog.NetworkTask;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class NetworkTaskManager {

    private AddKino addKino;
    private NetworkTaskCreator networkTaskCreator;
    private List<NetworkTask> taskList;

    public NetworkTaskManager(AddKino addKino) {
        this(addKino, new NetworkTaskCreator());
    }

    NetworkTaskManager(AddKino addKino, NetworkTaskCreator networkTaskCreator) {
        this.addKino = addKino;
        this.networkTaskCreator = networkTaskCreator;
        this.taskList = new ArrayList<>();
    }

    public void createAndExecute(Call<MovieResultsPage> call) {
        NetworkTask networkTask = networkTaskCreator.create(addKino);

        //noinspection unchecked
        networkTask.execute(call);
        taskList.add(networkTask);
    }

    public void cancelTasks() {
        for (NetworkTask task : taskList) {
            task.cancel(true);
        }
    }

    public List<NetworkTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<NetworkTask> taskList) {
        this.taskList = taskList;
    }
}
