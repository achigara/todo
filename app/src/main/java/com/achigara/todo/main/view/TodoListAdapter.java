package com.achigara.todo.main.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.achigara.todo.R;
import com.achigara.todo.databinding.TodoItemLayoutBinding;
import com.achigara.todo.main.model.TodoItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import static com.achigara.todo.main.view.TodoDetailsFragment.TAG;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoItemViewHolder> {

    private Fragment fragment;
    private LiveData<List<TodoItem>> itemList;

    TodoListAdapter(Fragment fragment, LiveData<List<TodoItem>> itemList) {
        this.fragment = fragment;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodoItemLayoutBinding todoItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_item_layout, parent, false);

        return new TodoItemViewHolder(todoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        if (itemList.getValue() != null) {
            holder.bind(itemList.getValue().get(position));
            holder.todoItemBinding.getRoot().setOnClickListener(view -> {
                if(fragment.getActivity() != null) {
                    fragment.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, TodoDetailsFragment.newInstance(itemList.getValue().get(position), false), TAG)
                            .addToBackStack(TAG)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (itemList.getValue() != null) {
            return itemList.getValue().size();
        }
        return 0;
    }

    static class TodoItemViewHolder extends RecyclerView.ViewHolder {
        TodoItemLayoutBinding todoItemBinding;

        TodoItemViewHolder(@NonNull TodoItemLayoutBinding todoItemBinding) {
            super(todoItemBinding.getRoot());
            this.todoItemBinding = todoItemBinding;
        }

        void bind(TodoItem item) {
            todoItemBinding.setTodoItem(item);
            todoItemBinding.executePendingBindings();
        }

    }
}
