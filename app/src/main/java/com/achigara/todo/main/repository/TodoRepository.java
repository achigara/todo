package com.achigara.todo.main.repository;

import android.app.Application;

import com.achigara.todo.main.db.TodoItemDB;
import com.achigara.todo.main.model.TodoItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class TodoRepository {

    private TodoItemDB todoItemDB;
    private LiveData<List<TodoItem>> todoItems;
    private static TodoRepository repository;


    private TodoRepository(Application application) {
        todoItemDB = TodoItemDB.getDatabase(application);
        loadTodoItems();
    }

    public static TodoRepository getRepository(Application application) {
        if (repository == null) {
            repository = new TodoRepository(application);
        }
        return repository;
    }

    private void loadTodoItems() {
        //TODO: get items from local source and synchronize with remote source
        todoItems = Transformations.map(getItemsFromLocalDataSource(), items -> items);
        getItemsFromRemoteDataSource();
    }

    private LiveData<List<TodoItem>> getItemsFromLocalDataSource() {
        return todoItemDB.todoItemDao().getItemsList();
    }

    private void getItemsFromRemoteDataSource() {
        //TODO: implement properly
    }

    public void addItem(TodoItem item) {
        new Thread(
                () -> todoItemDB.todoItemDao().insert(item)
        ).start();
    }

    public void updateItem(TodoItem item) {
        todoItemDB.todoItemDao().update(item);
    }

    public void deleteItem(TodoItem item) {
        todoItemDB.todoItemDao().delete(item);
    }

    public void deleteAllItems(TodoItem item) {
        todoItemDB.todoItemDao().delete(item);
    }

    public LiveData<List<TodoItem>> getTodoItems() {
        return todoItems;
    }
}
