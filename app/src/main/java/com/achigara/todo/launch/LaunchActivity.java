package com.achigara.todo.launch;

import android.content.Intent;
import android.os.Bundle;

import com.achigara.todo.main.MainActivity;
import com.achigara.todo.R;
import com.achigara.todo.auth.AuthenticationActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        //if user is not logged in go to the Authentication activity
        if(auth.getCurrentUser() == null){
            Intent intent = new Intent(this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        //if user is already logged in go straight to the main activity
        else{
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
