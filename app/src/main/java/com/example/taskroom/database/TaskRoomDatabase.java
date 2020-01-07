package com.example.taskroom.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.taskroom.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// increment version number if schema changes
@Database(entities={Task.class}, version=1)
public abstract class TaskRoomDatabase extends RoomDatabase {
    abstract TaskDao taskDao();

    private static final String LOG_TAG = TaskRoomDatabase.class.getSimpleName();

    // singleton to avoid multiple instances of the DB
    private static volatile TaskRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null){ INSTANCE = Room
                    .databaseBuilder( context.getApplicationContext(), TaskRoomDatabase.class, "task_database")
                    // write test data on onOpen or OnCreate
                    .addCallback(sRoomDatabaseCallback)  // write test data
                    // to avoid locking the UI, queries should be done in a separate thread
                    // allow only for testing
                    // .allowMainThreadQueries()
                    .build();
                }
            }
        }

        Log.d( LOG_TAG, "Returning a database instance");
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    TaskDao dao = INSTANCE.taskDao();

                    Log.d( LOG_TAG, "Deleting all tasks");
                    dao.deleteAllTasks();

                    Log.d( LOG_TAG, "Inserting test task");
                    Task task = new Task();
                    task.setTitle("First task");
                    dao.insertTask(task);

                    Log.d( LOG_TAG, "Inserting test task");
                    task = new Task();
                    task.setTitle("Second task");
                    dao.insertTask(task);
                }
            });
        }
    };

}
