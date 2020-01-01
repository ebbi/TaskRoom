package com.example.taskroom.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskroom.TaskApplication;
import com.example.taskroom.database.Task;
import com.example.taskroom.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();
    private TaskRepository mTaskRepository;

    private LiveData<List<Task>> mTasks;

    public MainViewModel(Application application) {
        super(application);
        Log.d( LOG_TAG, "MainViewModel");

        mTaskRepository = new TaskRepository(application);
        mTasks = mTaskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return mTasks;
    }

    public void insert( Task task ){
        mTaskRepository.insert(task);
    }
}
