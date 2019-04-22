package com.achigara.todo.main.db;

import com.achigara.todo.main.model.TodoItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TodoItemDao {

    @Query("SELECT * from todo_items ORDER BY id ASC")
    LiveData<List<TodoItem>> getItemsList();

    @Query("SELECT * from todo_items WHERE id=:itemId")
    TodoItem getItem(long itemId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TodoItem item);

    @Query("DELETE FROM todo_items")
    void deleteAll();

    @Delete
    void delete(TodoItem item);

    @Update
    int update(TodoItem item);
}
