package com.achigara.todo.auth.view;

import android.app.Application;

import com.achigara.todo.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AuthenticationFragmentTest {

    @Test
    public void emailErrorShowsWhenEmailIsInvalidWhenLogin() {
        Application application = ApplicationProvider.getApplicationContext();
        FragmentScenario.launchInContainer(AuthenticationFragment.class);

        onView(withId(R.id.emailView)).perform(typeText("aaa"));
        onView(withId(R.id.buttonLogin)).perform(click());

        onView(allOf(withId(R.id.textinput_error), withText(application.getString(R.string.error_invalid_email))))
                .check(matches(withText(application.getString(R.string.error_invalid_email))));
        onView(withText(application.getString(R.string.error_invalid_email))).check(matches(isDisplayed()));
        onView(withText(application.getString(R.string.error_invalid_password))).check(matches(isDisplayed()));
    }
}