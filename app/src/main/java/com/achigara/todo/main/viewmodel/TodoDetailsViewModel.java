package com.achigara.todo.main.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.achigara.todo.R;
import com.achigara.todo.common.Event;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.repository.TodoRepository;

import java.text.DecimalFormat;
import java.util.Calendar;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TodoDetailsViewModel extends AndroidViewModel {

    private TodoItem item;
    private MutableLiveData<Integer> action;

    private MutableLiveData<Long> id;
    private MutableLiveData<String> title;
    private MutableLiveData<String> description;
    private MutableLiveData<Integer> type;
    private MutableLiveData<Long> alarmTime;
    private MutableLiveData<Long> alarmInterval;
    private MutableLiveData<String> titleError;
    private MutableLiveData<String> alarmError;
    private MutableLiveData<Event<Boolean>> operationStatus;

    private TodoRepository todoRepository;

    TodoDetailsViewModel(Application application) {
        super(application);
        action = new MutableLiveData<>();

        id = new MutableLiveData<>();
        title = new MutableLiveData<>();
        description = new MutableLiveData<>();
        type = new MutableLiveData<>();
        alarmTime = new MutableLiveData<>();
        alarmInterval = new MutableLiveData<>();
        titleError = new MutableLiveData<>();
        alarmError = new MutableLiveData<>();
        operationStatus = new MutableLiveData<>();

        todoRepository = TodoRepository.getRepository(application);
    }

    public TodoItem getItem() {
        return item;
    }

    public void setItem(TodoItem item) {

        this.item = item;
        this.id.setValue(item.getId());
        this.title.setValue(item.getTitle());
        this.description.setValue(item.getDescription());
        this.type.setValue(item.getType());
        this.alarmTime.setValue(item.getAlarmTime());
        this.alarmInterval.setValue(item.getAlarmInterval());
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setTitle(MutableLiveData<String> title) {
        this.title = title;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public void setDescription(MutableLiveData<String> description) {
        this.description = description;
    }

    public MutableLiveData<Long> getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(MutableLiveData<Long> alarmTime) {
        this.alarmTime = alarmTime;
    }

    public MutableLiveData<Long> getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(MutableLiveData<Long> alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public LiveData<String> getTitleError() {
        return titleError;
    }

    public void setTitleErrorValue(String titleError) {
        this.titleError.setValue(titleError);
    }

    public LiveData<String> getAlarmError() {
        return alarmError;
    }

    public void setAlarmErrorValue(String alarmError) {
        this.alarmError.setValue(alarmError);
    }

    public LiveData<Event<Boolean>> getOperationStatus() {
        return operationStatus;
    }

    public LiveData<Integer> getAction() {
        return action;
    }

    public void setActionValue(int action) {
        this.action.setValue(action);
    }


    public LiveData<String> getHumanReadableAlarmDate() {

        return Transformations.map(alarmTime, alarmTime -> {
            if (alarmTime > 0) {
                DecimalFormat format = new DecimalFormat("#00");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmTime);

                String date = "";
                date += format.format(calendar.get(Calendar.DAY_OF_MONTH));
                date += "/";
                date += format.format(calendar.get(Calendar.MONTH + 1));
                date += "/";
                date += calendar.get(Calendar.YEAR);

                return date;
            }
            return "--/--/----";
        });
    }

    public LiveData<String> getHumanReadableAlarmTime() {

        return Transformations.map(alarmTime, alarmTime -> {
            if (alarmTime > 0) {
                DecimalFormat format = new DecimalFormat("#00");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmTime);

                String time = format.format(calendar.get(Calendar.HOUR_OF_DAY));
                time += ":";
                time += format.format(calendar.get(Calendar.MINUTE));

                return time;
            }

            return "--:--";
        });
    }

    public LiveData<String> getHumanReadableAlarmInterval() {

        return Transformations.map(alarmInterval, alarmInterval -> {
            if (alarmInterval > 0) {
                DecimalFormat format = new DecimalFormat("#00");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmInterval);

                String time = "Repeat every ";
                time += format.format(calendar.get(Calendar.HOUR_OF_DAY));
                time += "hours, ";
                time += format.format(calendar.get(Calendar.MINUTE));
                time += "minutes";

                return time;
            }

            return "";
        });
    }

    private boolean isValidItem(TodoItem item) {
        boolean isValid = true;

        if (item != null && TextUtils.isEmpty(item.getTitle())) {
            titleError.setValue(getApplication().getString(R.string.error_item_title));
            isValid = false;
        }

        if (item != null && item.getAlarmTime() > 0 && item.getAlarmTime() < Calendar.getInstance().getTimeInMillis()) {
            alarmError.setValue(getApplication().getString(R.string.error_item_alarm));
            isValid = false;
        }

        return isValid;
    }

    public void addItem() {
        TodoItem item = new TodoItem(title.getValue(), description.getValue(),
                type.getValue() != null ? type.getValue() : 0,
                alarmTime.getValue() != null ? alarmTime.getValue() : 0,
                alarmInterval.getValue() != null ? alarmInterval.getValue() : 0);

        if (isValidItem(item)) {
            todoRepository.addItem(item);
            operationStatus.setValue(new Event<>(true));
        }
    }

    public void updateItem() {
        TodoItem item = new TodoItem(
                id.getValue() != null ? id.getValue() : 0,
                title.getValue(),
                description.getValue(),
                type.getValue() != null ? type.getValue() : 0,
                alarmTime.getValue() != null ? alarmTime.getValue() : 0,
                alarmInterval.getValue() != null ? alarmInterval.getValue() : 0);

        if (isValidItem(item)) {
            todoRepository.updateItem(item);
            operationStatus.setValue(new Event<>(true));
        }
    }
}
