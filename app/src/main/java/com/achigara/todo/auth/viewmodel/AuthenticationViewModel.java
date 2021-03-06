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

public class AuthenticationViewModel extends AndroidViewModel implements OnCompleteListener<AuthResult> {

    private MutableLiveData<String> email;
    private MutableLiveData<String> password;
    private MutableLiveData<String> emailError;
    private MutableLiveData<String> passwordError;
    private MutableLiveData<String> authenticationError;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private AuthenticationManager authenticationManager;

    public AuthenticationViewModel(Application application) {
        super(application);
        firebaseUser = new MutableLiveData<>();
        email = new MutableLiveData<>();
        password = new MutableLiveData<>();
        emailError = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        authenticationError = new MutableLiveData<>();
        authenticationManager = new AuthenticationManager();
    }

    //Constructor used for unit tests
    public AuthenticationViewModel(Application application, String test) {
        super(application);
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

    public LiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }

    public boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }

    private boolean isValidCredentials() {
        boolean isValid = true;

        if (!isEmailValid(email.getValue())) {
            emailError.setValue(getApplication().getString(R.string.error_invalid_email));
            isValid = false;
        }
        if (!isPasswordValid(password.getValue())) {
            passwordError.setValue(getApplication().getString(R.string.error_invalid_password));
            isValid = false;
        }

        return isValid;
    }

    public void login() {
        authenticationError.setValue("");
        if (isValidCredentials()) {
            authenticationManager.performLogin(email.getValue(), password.getValue(), this);
        }
    }

    public void register() {
        authenticationError.setValue("");
        if (isValidCredentials()) {
            authenticationManager.performRegister(email.getValue(), password.getValue(), this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.e("AuthenticationViewModel", "success");
            firebaseUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            if(task.getException() != null && task.getException().getLocalizedMessage() != null)
                authenticationError.setValue(task.getException().getLocalizedMessage());
        }
    }
}
