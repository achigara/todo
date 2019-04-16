package com.achigara.todo.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.R;
import com.achigara.todo.main.model.TodoItem;
import com.achigara.todo.main.viewmodel.TodoListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;
import static com.achigara.todo.main.view.TodoDetailsFragment.TAG;

public class TodoListFragment extends Fragment {

    private RecyclerView todoList;
    private TodoListViewModel todoListViewModel;

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.todo_list_fragment, container, false);
        todoList = root.findViewById(R.id.todoList);
        todoList.setLayoutManager(new LinearLayoutManager(getContext()));
        todoList.setHasFixedSize(true);

        FloatingActionButton fab = root.findViewById(R.id.fab_add_todo_item);
        fab.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TodoDetailsFragment.newInstance(new TodoItem(), TodoDetailsFragment.ACTION_CREATE), TAG)
                        .addToBackStack(TAG)
                        .commit();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(todoList);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        todoListViewModel = ViewModelProviders.of(this).get(TodoListViewModel.class);

        todoList.setAdapter(new TodoListAdapter(this, todoListViewModel.getItemList()));

        Observer<List<TodoItem>> todoItemsObserver = list -> {
            if (todoList.getAdapter() != null) {
                todoList.getAdapter().notifyDataSetChanged();
            }
        };
        todoListViewModel.getItemList().observe(getViewLifecycleOwner(), todoItemsObserver);
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback{
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, LEFT | RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            todoListViewModel.deleteItem(todoListViewModel.getItemList().getValue().get(viewHolder.getAdapterPosition()));
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }
    }
}
