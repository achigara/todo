package com.achigara.todo.auth;

import com.achigara.todo.auth.viewmodel.AuthenticationViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LoginUnitTest {

    private AuthenticationViewModel authenticationViewModel;

    @Before
    public void init() {
        authenticationViewModel = new AuthenticationViewModel(ApplicationProvider.getApplicationContext(), "");
    }

    @Test
    public void emailNotEmpty (){
        assertThat(authenticationViewModel.isEmailValid("")).isFalse();
    }
    @Test
    public void emailFormatCorrect(){
        assertThat(authenticationViewModel.isEmailValid("abc")).isFalse();
        assertThat(authenticationViewModel.isEmailValid("abc@")).isFalse();
        assertThat(authenticationViewModel.isEmailValid("@")).isFalse();
        assertThat(authenticationViewModel.isEmailValid("abc.xyz")).isFalse();
        assertThat(authenticationViewModel.isEmailValid("abc@def")).isFalse();
        assertThat(authenticationViewModel.isEmailValid("abc@def.xyz")).isTrue();
        assertThat(authenticationViewModel.isEmailValid("abc@def.xy.z")).isTrue();
    }

    @Test
    public void passwordNotEmpty(){
        assertThat(authenticationViewModel.isPasswordValid("")).isFalse();
    }

    @Test
    public void passwordLongEnough(){
        assertThat(authenticationViewModel.isPasswordValid("a")).isFalse();
        assertThat(authenticationViewModel.isPasswordValid("12345678")).isTrue();
    }
}
