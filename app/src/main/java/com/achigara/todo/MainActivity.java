package com.achigara.todo;

import android.os.Bundle;

import com.achigara.todo.main.view.TodoListFragment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TodoListFragment.newInstance())
                    .commitNow();
        }
    }
}
