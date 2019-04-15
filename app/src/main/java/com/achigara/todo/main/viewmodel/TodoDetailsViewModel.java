package com.achigara.todo.main.viewmodel;

import android.app.Application;

import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.repository.TodoRepository;

import androidx.lifecycle.AndroidViewModel;

public class TodoDetailsViewModel extends AndroidViewModel {
    private TodoItem item;
    private TodoRepository todoRepository;

    TodoDetailsViewModel(Application application){
        super(application);
        todoRepository = TodoRepository.getRepository(application);
    }

    public TodoItem getItem() {
        return item;
    }

    public void setItem(TodoItem item) {
        this.item = item;
    }
}
