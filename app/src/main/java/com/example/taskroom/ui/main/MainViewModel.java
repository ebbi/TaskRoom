package com.example.taskroom.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskroom.database.Task;
import com.example.taskroom.database.TaskRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();
    private TaskRepository mTaskRepository;

    private LiveData<List<Task>> liveDataTasks;
    private List<Task> mTasks;
    private Task mTask;

    public MainViewModel(Application application) {
        super(application);
        Log.d( LOG_TAG, "MainViewModel getLiveDataTasks LiveData from Room database");

        mTaskRepository = new TaskRepository(application);
        liveDataTasks = mTaskRepository.getLiveDataTasks();
    }

    /* viewModel observers call this observable LiveData */
    LiveData<List<Task>> getLiveDataTasks() {
        return liveDataTasks;
    }

    void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    public List<Task> getTasks(){
        return mTasks;
    }

    public Task getTask() {
        return mTask;
    }

    public void setTask(Task task) {
        this.mTask = task;
    }

    void insert(Task task){
        mTaskRepository.insert(task);
        mTasks.add(task);
        mTask = task;
    }

    void delete(Task task){
        Log.d( LOG_TAG, "MainViewModel delete task "  + task.getTaskId());
        mTaskRepository.delete(task);
    }
}
