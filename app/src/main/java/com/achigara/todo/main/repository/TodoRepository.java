package com.achigara.todo.main.repository;

import android.app.Application;

import com.achigara.todo.common.Event;
import com.achigara.todo.main.db.TodoItemDB;
import com.achigara.todo.main.model.TodoItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TodoRepository {

    private static TodoRepository repository;
    private TodoItemDB todoItemDB;
    private LiveData<List<TodoItem>> todoItems;
    private MutableLiveData<Event<TodoItem>> insertedOrUpdatedItem;

    private TodoRepository(Application application) {
        todoItemDB = TodoItemDB.getDatabase(application);
        insertedOrUpdatedItem = new MutableLiveData<>();
        loadTodoItems();
    }

    public static TodoRepository getRepository(Application application) {
        if (repository == null) {
            repository = new TodoRepository(application);
        }
        return repository;
    }

    public LiveData<Event<TodoItem>> getInsertedOrUpdatedItem() {
        return insertedOrUpdatedItem;
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
                () -> {
                    long id = todoItemDB.todoItemDao().insert(item);
                    item.setId(id);
                    insertedOrUpdatedItem.postValue(new Event<>(item));
                }
        ).start();
    }

    public void updateItem(TodoItem item) {
        new Thread(
                () -> {
                    int numberOfRowsChanged = todoItemDB.todoItemDao().update(item);
                    if (numberOfRowsChanged > 0) {
                        insertedOrUpdatedItem.postValue(new Event<>(item));
                    }
                }
        ).start();
    }

    public void deleteItem(TodoItem item) {
        new Thread(
                () -> todoItemDB.todoItemDao().delete(item)
        ).start();
    }

    public void deleteAllItems() {
        new Thread(
                () -> todoItemDB.todoItemDao().deleteAll()
        ).start();
    }

    public LiveData<List<TodoItem>> getTodoItems() {
        return todoItems;
    }
}
