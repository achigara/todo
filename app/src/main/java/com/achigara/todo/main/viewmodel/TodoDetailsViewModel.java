package com.achigara.todo.main.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.achigara.todo.R;
import com.achigara.todo.common.Event;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.repository.TodoRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TodoDetailsViewModel extends AndroidViewModel {

    private TodoItem item;
    private TodoRepository todoRepository;
    private MutableLiveData<String> titleError;
    private MutableLiveData<Event<Boolean>> operationStatus;

    TodoDetailsViewModel(Application application) {
        super(application);
        todoRepository = TodoRepository.getRepository(application);
        titleError = new MutableLiveData<>();
        operationStatus = new MutableLiveData<>();
    }

    public TodoItem getItem() {
        return item;
    }

    public void setItem(TodoItem item) {
        this.item = item;
    }

    public MutableLiveData<String> getTitleError() {
        return titleError;
    }

    public void setTitleErrorValue(String titleError) {
        this.titleError.setValue(titleError);
    }

    public LiveData<Event<Boolean>> getOperationStatus() {
        return operationStatus;
    }

    private boolean isValidItem(){
        boolean isValid = true;

        if(TextUtils.isEmpty(item.getTitle())){
            titleError.setValue(getApplication().getString(R.string.error_item_title));
            isValid = false;
        }

        return isValid;
    }

    public void addItem() {
        if(isValidItem()) {
            todoRepository.addItem(item);
            operationStatus.setValue(new Event<>(true));
        }
    }

    public void updateItem() {
        if(isValidItem()) {
            todoRepository.updateItem(item);
        }
    }
}
