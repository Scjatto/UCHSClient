package com.example.uchs;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityTestRule.getActivity();
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