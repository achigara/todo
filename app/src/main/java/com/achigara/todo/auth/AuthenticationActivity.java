package com.achigara.todo.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.achigara.todo.R;
import com.achigara.todo.auth.view.AuthenticationFragment;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AuthenticationFragment.newInstance())
                    .commitNow();
        }
    }
}
