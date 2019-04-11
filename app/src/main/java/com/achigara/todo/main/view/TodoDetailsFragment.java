package com.achigara.todo.main.view;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.R;
import com.achigara.todo.main.viewmodel.TodoDetailsViewModel;

public class TodoDetailsFragment extends Fragment {

    private TodoDetailsViewModel mViewModel;

    public static TodoDetailsFragment newInstance() {
        return new TodoDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todo_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TodoDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}
