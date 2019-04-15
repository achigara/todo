package com.achigara.todo.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.R;
import com.achigara.todo.common.Event;
import com.achigara.todo.databinding.TodoDetailsFragmentBinding;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.viewmodel.TodoDetailsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class TodoDetailsFragment extends Fragment {

    static final String TAG = TodoDetailsFragment.class.getSimpleName();

    public static final int ACTION_VIEW = 1;
    public static final int ACTION_CREATE = 2;
    public static final int ACTION_UPDATE = 3;

    private static final String TODO_ITEM = "todo_item";
    private static final String ACTION = "action";

    private TodoItem todoItem;
    private int action;

    private TodoDetailsFragmentBinding todoDetailsFragmentBinding;

    public TodoDetailsFragment() {
        // Required empty public constructor
    }

    static TodoDetailsFragment newInstance(TodoItem todoItem, int action) {
        TodoDetailsFragment fragment = new TodoDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(TODO_ITEM, todoItem);
        args.putInt(ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todoItem = getArguments().getParcelable(TODO_ITEM);
            action = getArguments().getInt(ACTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        todoDetailsFragmentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.todo_details_fragment, container, false);
        todoDetailsFragmentBinding.setAction(action);

        todoDetailsFragmentBinding.cancel.setOnClickListener(view -> {
            if(getActivity() != null){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return todoDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        todoDetailsFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());

        TodoDetailsViewModel todoDetailsViewModel = ViewModelProviders.of(this).get(TodoDetailsViewModel.class);
        todoDetailsViewModel.setItem(todoItem);

        todoDetailsFragmentBinding.setViewmodel(todoDetailsViewModel);
        todoDetailsFragmentBinding.save.setOnClickListener(view -> {
            todoDetailsViewModel.setTitleErrorValue("");
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

        Observer<Event<Boolean>> observeOperationState = event -> {
            if(event.getContentIfNotHandled() && getActivity() != null){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        todoDetailsViewModel.getOperationStatus().observe(getViewLifecycleOwner(), observeOperationState);
    }

}
