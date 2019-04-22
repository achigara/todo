package com.achigara.todo.main.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.achigara.todo.R;
import com.achigara.todo.common.Event;
import com.achigara.todo.databinding.TodoDetailsFragmentBinding;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.viewmodel.TodoDetailsViewModel;
import com.google.gson.Gson;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class TodoDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int ACTION_VIEW = 1;
    public static final int ACTION_CREATE = 2;
    public static final int ACTION_UPDATE = 3;
    static final String TAG = TodoDetailsFragment.class.getSimpleName();
    private static final String TODO_ITEM = "todo_item";
    private static final String ACTION = "action";

    private Menu menu;

    private TodoItem todoItem;
    private int action;

    private TodoDetailsFragmentBinding todoDetailsFragmentBinding;
    private TodoDetailsViewModel todoDetailsViewModel;

    public TodoDetailsFragment() {
        // Required empty public constructor
    }

    static TodoDetailsFragment newInstance(TodoItem todoItem, int action) {
        TodoDetailsFragment fragment = new TodoDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TODO_ITEM, new Gson().toJson(todoItem));
        args.putInt(ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todoItem = new Gson().fromJson(getArguments().getString(TODO_ITEM), TodoItem.class);
            action = getArguments().getInt(ACTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        todoDetailsFragmentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.todo_details_fragment, container, false);
        setHasOptionsMenu(true);
        return todoDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        todoDetailsFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());

        todoDetailsViewModel = ViewModelProviders.of(this).get(TodoDetailsViewModel.class);
        todoDetailsViewModel.setItem(todoItem);
        todoDetailsViewModel.setActionValue(action);

        todoDetailsFragmentBinding.setViewmodel(todoDetailsViewModel);

        todoDetailsFragmentBinding.pickDate.setOnClickListener(view -> {
            if (getContext() != null) {
                Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        todoDetailsFragmentBinding.pickTime.setOnClickListener(view -> {
            if (getContext() != null) {
                Calendar date = Calendar.getInstance();
                new TimePickerDialog(getContext(), this, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext())).show();
            }
        });

        todoDetailsFragmentBinding.hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                todoDetailsViewModel.setRepeatAlarmErrorValue("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && Integer.valueOf(s.toString()) > 23) {
                    s.replace(0, s.length(), "23");
                }
            }
        });

        todoDetailsFragmentBinding.minutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                todoDetailsViewModel.setRepeatAlarmErrorValue("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && Integer.valueOf(s.toString()) > 59) {
                    s.replace(0, s.length(), "59");
                }
            }
        });

        todoDetailsFragmentBinding.save.setOnClickListener(view -> {
            todoDetailsViewModel.setTitleErrorValue("");
            todoDetailsViewModel.setAlarmErrorValue("");
            todoDetailsViewModel.setRepeatAlarmErrorValue("");
            int action = todoDetailsViewModel.getAction().getValue() != null ? todoDetailsViewModel.getAction().getValue() : 0;
            switch (action) {
                case ACTION_CREATE: {
                    todoDetailsViewModel.addItem();
                    break;
                }
                case ACTION_UPDATE: {
                    todoDetailsViewModel.updateItem();
                    break;
                }
            }
        });

        todoDetailsFragmentBinding.cancel.setOnClickListener(view -> {
            int action = todoDetailsViewModel.getAction().getValue() != null ? todoDetailsViewModel.getAction().getValue() : 0;
            if (action == ACTION_UPDATE) {
                todoDetailsViewModel.setItem(todoDetailsViewModel.getItem());
                todoDetailsViewModel.setTitleErrorValue("");
                todoDetailsViewModel.setAlarmErrorValue("");
                todoDetailsViewModel.setActionValue(ACTION_VIEW);
                stopObserveAlarm();
                stopObserveTitle();
                if (menu != null && menu.findItem(R.id.edit) != null) {
                    menu.findItem(R.id.edit).setVisible(true);
                }
            } else {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, TodoListFragment.newInstance())
                            .commitNow();
                }
            }
        });

        if (todoDetailsViewModel.getAction().getValue() != null && todoDetailsViewModel.getAction().getValue() != ACTION_VIEW) {
            observeTitle();
            observeAlarm();
        }

        Observer<Event<Boolean>> observeOperationState = event -> {
            if (event.getContentIfNotHandled() && getActivity() != null) {
                new Handler().post(() -> getActivity().getSupportFragmentManager().popBackStack()
                );
            }
        };
        todoDetailsViewModel.getOperationStatus().observe(getViewLifecycleOwner(), observeOperationState);
    }

    private void observeTitle() {
        Observer<String> titleObserver = title -> todoDetailsViewModel.setTitleErrorValue("");
        todoDetailsViewModel.getTitle().observe(getViewLifecycleOwner(), titleObserver);
    }

    private void observeAlarm() {
        Observer<Long> alarmTimeObserver = alarmTime -> {
            if (alarmTime > Calendar.getInstance().getTimeInMillis()) {
                todoDetailsViewModel.setAlarmErrorValue("");
            }
        };
        todoDetailsViewModel.getAlarmTime().observe(getViewLifecycleOwner(), alarmTimeObserver);
    }

    private void stopObserveTitle() {
        todoDetailsViewModel.getTitle().removeObservers(getViewLifecycleOwner());
    }

    private void stopObserveAlarm() {
        todoDetailsViewModel.getAlarmTime().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item_details, menu);
        this.menu = menu;

        MenuItem item = menu.findItem(R.id.edit);
        int action = todoDetailsViewModel.getAction().getValue() != null ? todoDetailsViewModel.getAction().getValue() : 0;
        item.setVisible(action == ACTION_VIEW);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit: {
                todoDetailsViewModel.setActionValue(ACTION_UPDATE);

                observeAlarm();
                observeTitle();

                item.setVisible(false);
                return false;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar newDate = Calendar.getInstance();
        newDate.clear();
        newDate.set(year, month, dayOfMonth);

        if (todoDetailsViewModel.getAlarmTime().getValue() != null) {
            Calendar oldDate = Calendar.getInstance();
            oldDate.setTimeInMillis(todoDetailsViewModel.getAlarmTime().getValue());

            newDate.set(Calendar.MINUTE, oldDate.get(Calendar.MINUTE));
            newDate.set(Calendar.HOUR_OF_DAY, oldDate.get(Calendar.HOUR_OF_DAY));
        }

        todoDetailsViewModel.getAlarmTime().setValue(newDate.getTimeInMillis());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar newDate = Calendar.getInstance();
        newDate.clear();
        newDate.set(Calendar.MINUTE, minute);
        newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);

        if (todoDetailsViewModel.getAlarmTime().getValue() != null) {
            Calendar oldDate = Calendar.getInstance();
            oldDate.setTimeInMillis(todoDetailsViewModel.getAlarmTime().getValue());
            newDate.set(oldDate.get(Calendar.YEAR), oldDate.get(Calendar.MONTH), oldDate.get(Calendar.DAY_OF_MONTH));
        }

        todoDetailsViewModel.getAlarmTime().setValue(newDate.getTimeInMillis());
    }
}
