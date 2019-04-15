package com.achigara.todo.main.viewmodel;

import android.app.Application;

import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.repository.TodoRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TodoListViewModel extends AndroidViewModel {

    private LiveData<List<TodoItem>> itemList;
    private TodoRepository todoRepository;

    public TodoListViewModel(Application application) {
        super(application);
        itemList = new MutableLiveData<>();
        todoRepository = TodoRepository.getRepository(application);

        itemList = Transformations.map(todoRepository.getTodoItems(), input -> input);
    }

    public LiveData<List<TodoItem>> getItemList() {
        return itemList;
    }

    public void addItem(TodoItem item){
        todoRepository.addItem(item);
    }

    public void deleteItem(TodoItem item){
        todoRepository.deleteItem(item);
    }

    public void updateItem(TodoItem item){
        todoRepository.updateItem(item);
    }
}
