package com.achigara.todo.main.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import com.achigara.todo.R;
import com.achigara.todo.common.Event;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.notifications.NotificationsManager;
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
    private MutableLiveData<Boolean> hasAlarm;
    private MutableLiveData<Long> alarmTime;
    private MutableLiveData<Boolean> repeatAlarm;
    private MutableLiveData<Long> alarmInterval;
    private MutableLiveData<String> titleError;
    private MutableLiveData<String> alarmError;
    private MutableLiveData<String> repeatHours;
    private MutableLiveData<String> repeatMinutes;
    private MutableLiveData<String> repeatAlarmError;

    private LiveData<Event<Boolean>> operationStatus;

    private TodoRepository todoRepository;

    TodoDetailsViewModel(Application application) {
        super(application);
        action = new MutableLiveData<>();

        id = new MutableLiveData<>();
        title = new MutableLiveData<>();
        description = new MutableLiveData<>();
        hasAlarm = new MutableLiveData<>();
        alarmTime = new MutableLiveData<>();
        repeatAlarm = new MutableLiveData<>();
        alarmInterval = new MutableLiveData<>();
        titleError = new MutableLiveData<>();
        alarmError = new MutableLiveData<>();
        repeatHours = new MutableLiveData<>();
        repeatMinutes = new MutableLiveData<>();
        repeatAlarmError = new MutableLiveData<>();

        operationStatus = new MutableLiveData<>();

        todoRepository = TodoRepository.getRepository(application);
        observeItemInsertsAndUpdates();
    }

    public TodoItem getItem() {
        return item;
    }

    public void setItem(TodoItem item) {

        this.item = item;
        this.id.setValue(item.getId());
        this.title.setValue(item.getTitle());
        this.description.setValue(item.getDescription());
        this.alarmTime.setValue(item.getAlarmTime());
        this.alarmInterval.setValue(item.getAlarmInterval());
        this.hasAlarm.setValue(item.getAlarmTime() > 0);
        this.repeatAlarm.setValue(item.getAlarmTime() > 0 && item.getAlarmInterval() > 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getAlarmInterval());
        repeatHours.setValue(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        repeatMinutes.setValue(String.valueOf(calendar.get(Calendar.MINUTE)));
    }

    public LiveData<Long> getId() {
        return id;
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

    public MutableLiveData<Boolean> getHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(MutableLiveData<Boolean> hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public MutableLiveData<Long> getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(MutableLiveData<Long> alarmTime) {
        this.alarmTime = alarmTime;
    }

    public MutableLiveData<Boolean> getRepeatAlarm() {
        return repeatAlarm;
    }

    public void setRepeatAlarm(MutableLiveData<Boolean> repeatAlarm) {
        this.repeatAlarm = repeatAlarm;
    }

    public MutableLiveData<String> getRepeatHours() {
        return repeatHours;
    }

    public void setRepeatHours(MutableLiveData<String> repeatHours) {
        this.repeatHours = repeatHours;
    }

    public MutableLiveData<String> getRepeatMinutes() {
        return repeatMinutes;
    }

    public void setRepeatMinutes(MutableLiveData<String> repeatMinutes) {
        this.repeatMinutes = repeatMinutes;
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

    public MutableLiveData<String> getRepeatAlarmError() {
        return repeatAlarmError;
    }

    public void setRepeatAlarmErrorValue(String repeatAlarmError) {
        this.repeatAlarmError.setValue(repeatAlarmError);
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

    public LiveData<Integer> getAlarmIntervalHours() {
        return Transformations.map(alarmInterval, alarmInterval -> {
            if (alarmInterval > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmInterval);

                return calendar.get(Calendar.HOUR_OF_DAY);
            }

            return 0;
        });
    }

    public LiveData<Integer> getAlarmIntervalMinutes() {
        return Transformations.map(alarmInterval, alarmInterval -> {
            if (alarmInterval > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmInterval);

                return calendar.get(Calendar.MINUTE);
            }

            return 0;
        });
    }

    private boolean isValidItem() {
        boolean isValid = true;

        if (TextUtils.isEmpty(title.getValue())) {
            titleError.setValue(getApplication().getString(R.string.error_item_title));
            isValid = false;
        }

        if (hasAlarm.getValue() != null) {
            if (alarmTime.getValue() != null && alarmTime.getValue() < Calendar.getInstance().getTimeInMillis()) {
                alarmError.setValue(getApplication().getString(R.string.error_item_alarm));
                isValid = false;
            }

            if (repeatAlarm.getValue() != null && repeatAlarm.getValue()) {
                if (alarmInterval.getValue() != null && alarmInterval.getValue() <= 0) {
                    repeatAlarmError.setValue(getApplication().getString(R.string.error_repeat_alarm));
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private void setAlarmIntervalValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(repeatHours.getValue()));
        calendar.set(Calendar.MINUTE, Integer.valueOf(repeatMinutes.getValue()));
        alarmInterval.setValue(calendar.getTimeInMillis());
    }

    public void addItem() {
        setAlarmIntervalValue();
        if (isValidItem()) {
            TodoItem item = new TodoItem(
                    title.getValue(),
                    description.getValue(),
                    hasAlarm.getValue() != null && hasAlarm.getValue() &&
                            alarmTime.getValue() != null ? alarmTime.getValue() : 0,
                    hasAlarm.getValue() != null && hasAlarm.getValue() &&
                            repeatAlarm.getValue() != null && repeatAlarm.getValue() &&
                            alarmInterval.getValue() != null ? alarmInterval.getValue() : 0);
            todoRepository.addItem(item);
        }
    }

    public void updateItem() {
        setAlarmIntervalValue();
        if (isValidItem()) {
            TodoItem item = new TodoItem(
                    id.getValue() != null ? id.getValue() : 0,
                    title.getValue(),
                    description.getValue(),
                    hasAlarm.getValue() != null && hasAlarm.getValue() &&
                            alarmTime.getValue() != null ? alarmTime.getValue() : 0,
                    hasAlarm.getValue() != null && hasAlarm.getValue() &&
                            repeatAlarm.getValue() != null && repeatAlarm.getValue() &&
                            alarmInterval.getValue() != null ? alarmInterval.getValue() : 0);
            todoRepository.updateItem(item);
        }
    }

    private void observeItemInsertsAndUpdates() {
        operationStatus = Transformations.map(todoRepository.getInsertedOrUpdatedItem(), event -> {
            TodoItem item = event.getContentIfNotHandled();
            if (item != null) {
                if (item.hasAlarm()) {
                    setNotification(item);
                }
                return new Event<>(true);
            }

            return new Event<>(false);
        });
    }

    private void setNotification(TodoItem item) {
        NotificationsManager.getInstance(getApplication()).scheduleNotification(item);
    }
}
