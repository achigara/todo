package com.achigara.todo.auth.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {

    private FirebaseAuth auth;

    public LoginRepository() {
        auth = FirebaseAuth.getInstance();
    }

    public void performLogin(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> onCompleteListener){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
}
