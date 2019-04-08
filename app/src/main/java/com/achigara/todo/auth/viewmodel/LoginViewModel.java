package com.achigara.todo.auth.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.achigara.todo.R;
import com.achigara.todo.auth.repository.AuthenticationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel implements OnCompleteListener<AuthResult> {

    private MutableLiveData<String> email;
    private MutableLiveData<String> password;
    private MutableLiveData<String> emailError;
    private MutableLiveData<String> passwordError;
    private MutableLiveData<String> authenticationError;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private AuthenticationManager authenticationManager;

    public LoginViewModel(Application application) {
        super(application);
        firebaseUser = new MutableLiveData<>();
        email = new MutableLiveData<>();
        password = new MutableLiveData<>();
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        authenticationError = new MutableLiveData<>();

        authenticationManager = new AuthenticationManager();

        email.setValue("amin.chigara@yatechnologies.com");
        password.setValue("12345678");
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getEmailError() {
        return emailError;
    }

    public MutableLiveData<String> getPasswordError() {
        return passwordError;
    }

    public MutableLiveData<String> getAuthenticationError() {
        return authenticationError;
    }

    public boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }

    public LiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    private boolean hasCredentialErrors() {
        boolean hasErrors = false;

        if (!isEmailValid(email.getValue())) {
            emailError.setValue(getApplication().getString(R.string.error_invalid_email));
            hasErrors = true;
        }
        if (!isPasswordValid(password.getValue())) {
            passwordError.setValue(getApplication().getString(R.string.error_invalid_password));
            hasErrors = true;
        }

        return hasErrors;
    }

    public void login() {
        authenticationError.setValue("");
        if (!hasCredentialErrors()) {
            authenticationManager.performLogin(email.getValue(), password.getValue(), this);
        }
    }

    public void register() {
        authenticationError.setValue("");
        if (!hasCredentialErrors()) {
            authenticationManager.performRegister(email.getValue(), password.getValue(), this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.e("LoginViewModel", "success");
            firebaseUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            authenticationError.setValue(task.getException().getLocalizedMessage());
        }
    }
}
