package com.example.taskroom.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private static final String LOG_TAG = TaskRepository.class.getSimpleName();

    private TaskDao mTaskDao;
    private LiveData<List<Task>> mTasks;

    public TaskRepository(Application application) {
        Log.d( LOG_TAG, "TaskRepository");
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        mTaskDao = taskRoomDatabase.taskDao();
        mTasks = mTaskDao.getTasks();
    }

    /* LiveData executes all queries on a separate thread.
       Observed LiveData will notify the observer when the data has changed. */
    public LiveData<List<Task>> getTasks(){
        return mTasks;
    }

    /* You must call this on a non-UI thread or your app will crash.
       this way, Room ensures that you're not doing any long running operations on the main
       thread, blocking the UI. */
    public void insert (final Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.insertTask(task);
            }
        });
    }

}
