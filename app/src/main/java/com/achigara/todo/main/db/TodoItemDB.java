package com.achigara.todo.main.db;

import android.content.Context;

import com.achigara.todo.main.model.TodoItem;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TodoItem.class}, version = 3, exportSchema = false)
public abstract class TodoItemDB extends RoomDatabase {

    private static TodoItemDB INSTANCE;

    public static TodoItemDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoItemDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoItemDB.class, "todo_item_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TodoItemDao todoItemDao();
}
