package com.achigara.todo.main.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.R;
import com.achigara.todo.databinding.TodoDetailsFragmentBinding;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.viewmodel.TodoDetailsViewModel;

public class TodoDetailsFragment extends Fragment {

    static final String TAG = TodoDetailsFragment.class.getSimpleName();

    private static final String TODO_ITEM = "todo_item";
    private static final String EDIT = "edit";

    private TodoItem todoItem;
    private boolean edit;

    private TodoDetailsFragmentBinding todoDetailsFragmentBinding;

    public TodoDetailsFragment() {
        // Required empty public constructor
    }

    static TodoDetailsFragment newInstance(TodoItem todoItem, boolean edit) {
        TodoDetailsFragment fragment = new TodoDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(TODO_ITEM, todoItem);
        args.putBoolean(EDIT, edit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todoItem = getArguments().getParcelable(TODO_ITEM);
            edit = getArguments().getBoolean(EDIT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        todoDetailsFragmentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.todo_details_fragment, container, false);
        todoDetailsFragmentBinding.setEdit(edit);

        return todoDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TodoDetailsViewModel todoDetailsViewModel = ViewModelProviders.of(this).get(TodoDetailsViewModel.class);
        todoDetailsViewModel.setItem(todoItem);
        todoDetailsFragmentBinding.setViewmodel(todoDetailsViewModel);
    }

}
