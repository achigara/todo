package com.achigara.todo.auth.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationManager {

    private FirebaseAuth auth;

    public AuthenticationManager() {
        auth = FirebaseAuth.getInstance();
    }

    public void performLogin(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> onCompleteListener){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public void performRegister(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> onCompleteListener){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
}
