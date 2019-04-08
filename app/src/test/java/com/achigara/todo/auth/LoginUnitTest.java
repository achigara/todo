package com.achigara.todo.auth;

import com.achigara.todo.auth.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class LoginUnitTest {

    LoginViewModel loginViewModel;

    @Before
    public void init() {
        loginViewModel = new LoginViewModel("", "");
    }

    @Test
    public void emailNotEmpty (){
        assertFalse(loginViewModel.isEmailValid(""));
    }
    @Test
    public void emailFormatCorrect(){
        assertFalse(loginViewModel.isEmailValid("abc"));
        assertFalse(loginViewModel.isEmailValid("abc@"));
        assertFalse(loginViewModel.isEmailValid("@"));
        assertFalse(loginViewModel.isEmailValid("abc.xyz"));
        assertFalse(loginViewModel.isEmailValid("abc@def"));
        assertTrue(loginViewModel.isEmailValid("abc@def.xyz"));
        assertTrue(loginViewModel.isEmailValid("abc@def.xy.z"));
    }

    @Test
    public void passwordNotEmpty(){
        assertFalse(loginViewModel.isPasswordValid(""));
    }

    @Test
    public void passwordLongEnough(){
        assertFalse(loginViewModel.isPasswordValid("a"));
        assertTrue(loginViewModel.isPasswordValid("12345678"));
    }
}
