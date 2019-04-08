package com.achigara.todo.auth.viewmodel;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.achigara.todo.auth.repository.LoginRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel implements OnCompleteListener<AuthResult> {

    private String email;
    private String password;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private LoginRepository loginRepository;

    public LoginViewModel() {
        firebaseUser = new MutableLiveData<>();
        loginRepository = new LoginRepository();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailValid(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isPasswordValid(String password){
        return !TextUtils.isEmpty(password) && password.length()>=8;
    }

    public LiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    public void login(){
        if(isEmailValid(email) && isPasswordValid(password)){
            loginRepository.performLogin(email, password, this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            Log.e("LoginViewModel", "success");
            firebaseUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
        }
        else{
            Log.e("LoginViewModel", "failure");
        }
    }
}
