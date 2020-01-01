package com.example.taskroom.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// increment version number if schema changes
@Database(entities={Task.class}, version=1)
abstract class TaskRoomDatabase extends RoomDatabase {
    abstract TaskDao taskDao();

    // singleton to avoid multiple instances of the DB
    private static volatile TaskRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null){
                    INSTANCE = Room
                            .databaseBuilder( context.getApplicationContext(), TaskRoomDatabase.class, "task_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
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
                    dao.deleteAllTasks();

                    Task task = new Task();
                    task.setTitle("First task");
                    dao.insertTask(task);
                    task = new Task();
                    task.setTitle("Second task");
                    dao.insertTask(task);
                }
            });
        }
    };

}
