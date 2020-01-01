package com.example.taskroom.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY taskId")
    LiveData<List<Task>> getTasks();

    @Query("DELETE FROM Task")
    void deleteAllTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

/*
    @Query("SELECT * FROM Task where taskId = :id")
    Task getTaskById(long id);

    @Query("SELECT * FROM Task WHERE taskId > :id ORDER BY :id LIMIT 1")
    Task getNextTask(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(List<Task> tasks);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Delete
    void deleteTasks(List<Task> tasks);


 */

}
