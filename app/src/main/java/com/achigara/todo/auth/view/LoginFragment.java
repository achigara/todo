package com.achigara.todo.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        loginFragmentBinding.buttonLogin.setOnClickListener(this::performLogin);
        loginFragmentBinding.setLifecycleOwner(getViewLifecycleOwner());
        return loginFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginFragmentBinding.setViewmodel(loginViewModel);

        Observer<FirebaseUser> userObserver = user -> {
            if(user != null){
                navigateToMainActivity();
            }
        };

        loginViewModel.getFirebaseUser().observe(getViewLifecycleOwner(), userObserver);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            navigateToMainActivity();
        }
    }

    private void navigateToMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getContext().startActivity(intent);
    }

    private void performLogin(View view){
        loginViewModel.login();
    }

}
