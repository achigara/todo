package com.achigara.todo.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achigara.todo.MainActivity;
import com.achigara.todo.R;
import com.achigara.todo.auth.viewmodel.AuthenticationViewModel;
import com.achigara.todo.databinding.AuthenticationFragmentBinding;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AuthenticationFragment extends Fragment {

    private AuthenticationViewModel authenticationViewModel;
    private AuthenticationFragmentBinding authenticationFragmentBinding;

    public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        authenticationFragmentBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.authentication_fragment, container, false);
        authenticationFragmentBinding.buttonLogin.setOnClickListener(view -> performLogin());
        authenticationFragmentBinding.buttonRegister.setOnClickListener(view -> performRegister());
        return authenticationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        authenticationFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());
        authenticationViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        authenticationFragmentBinding.setViewmodel(authenticationViewModel);

        //when the user successfully logs in go to main activity
        Observer<FirebaseUser> userObserver = user -> {
            if (user != null) {
                navigateToMainActivity();
            }
        };
        authenticationViewModel.getFirebaseUser().observe(getViewLifecycleOwner(), userObserver);

        //clear email error when email changes
        Observer<String> emailObserver = email -> authenticationFragmentBinding.emailInputLayout.setError(null);
        authenticationViewModel.getEmail().observe(getViewLifecycleOwner(), emailObserver);

        //clear password error when password changes
        Observer<String> passwordObserver = password -> authenticationFragmentBinding.passwordInputLayout.setError(null);
        authenticationViewModel.getPassword().observe(getViewLifecycleOwner(), passwordObserver);
    }

    private void navigateToMainActivity() {
        if(getContext()!= null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
        }
    }

    private void performLogin() {
        authenticationViewModel.login();
    }

    private void performRegister() {
        authenticationViewModel.register();
    }

}
