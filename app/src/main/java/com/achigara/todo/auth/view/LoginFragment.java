package com.achigara.todo.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.MainActivity;
import com.achigara.todo.R;
import com.achigara.todo.auth.viewmodel.LoginViewModel;
import com.achigara.todo.databinding.LoginFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private LoginFragmentBinding loginFragmentBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.login_fragment, container, false);
        loginFragmentBinding.buttonLogin.setOnClickListener(view -> performLogin());
        loginFragmentBinding.buttonRegister.setOnClickListener(view -> performRegister());
        loginFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());
        return loginFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginFragmentBinding.setViewmodel(loginViewModel);

        //when the user successfully logs in go to main activity
        Observer<FirebaseUser> userObserver = user -> {
            if (user != null) {
                navigateToMainActivity();
            }
        };
        loginViewModel.getFirebaseUser().observe(getViewLifecycleOwner(), userObserver);

        //clear email error when email changes
        Observer<String> emailObserver = email -> loginFragmentBinding.emailInputLayout.setError(null);
        loginViewModel.getEmail().observe(getViewLifecycleOwner(), emailObserver);

        //clear password error when password changes
        Observer<String> passwordObserver = password -> loginFragmentBinding.passwordInputLayout.setError(null);
        loginViewModel.getPassword().observe(getViewLifecycleOwner(), passwordObserver);

        //if user is already logged in go straight to the main activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navigateToMainActivity();
        }
    }

    private void navigateToMainActivity() {
        if(getContext()!= null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    }

    private void performLogin() {
        loginViewModel.login();
    }

    private void performRegister() {
        loginViewModel.register();
    }

}
