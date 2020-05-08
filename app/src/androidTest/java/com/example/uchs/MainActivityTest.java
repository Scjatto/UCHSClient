package com.example.uchs;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;
    Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation()
            .addMonitor(LoginActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityTestRule.getActivity();
    }


    @Test
    public void goToLoginActivity() {
        View viewBtLogin = mainActivity.findViewById(R.id.btLogin);
        assertNotNull(viewBtLogin);
        onView(ViewMatchers.withId(R.id.btLogin)).perform(click());
        Activity loginActivity = InstrumentationRegistry.getInstrumentation().waitForMonitor(monitor);
        assertNotNull(loginActivity);
        loginActivity.finish();
    }

    @Test
    public void mainActivityLaunch() {
        View viewBtRegister = mainActivity.findViewById(R.id.btRegister);
        assertNotNull(viewBtRegister);

        View viewBtLogin = mainActivity.findViewById(R.id.btLogin);
        assertNotNull(viewBtLogin);
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}