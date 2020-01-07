package com.example.taskroom.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private static final String LOG_TAG = TaskRepository.class.getSimpleName();

    private TaskDao mTaskDao;
    private LiveData<List<Task>> mLiveDataTasks;

    public TaskRepository(Application application) {
        Log.d( LOG_TAG, "TaskRepository");
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        mTaskDao = taskRoomDatabase.taskDao();
        mLiveDataTasks = mTaskDao.getLiveDataTasks();
    }

    /* LiveData executes all queries on a separate thread.
       Observed LiveData will notify the observer when the data has changed. */
    public LiveData<List<Task>> getLiveDataTasks(){
        return mLiveDataTasks;
    }

    public void insert (final Task task) {
        Log.d( LOG_TAG, "TaskRepository insert " + task.getTitle());
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.insertTask(task);
            }
        });
    }

    public void delete (final Task task) {
        Log.d( LOG_TAG, "TaskRepository delete " + task.getTitle());
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteTask(task);
            }
        });
    }


}
