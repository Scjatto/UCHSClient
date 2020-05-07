package com.example.uchs;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity loginActivity = null;

    @Before
    public void setUp() throws Exception {
        loginActivity = loginActivityTestRule.getActivity();
    }

    @Test
    public void launchLoginActivity() {
        View editIdPhone = loginActivity.findViewById(R.id.editIdPhone);
        assertNotNull(editIdPhone);

        View btLogin = loginActivity.findViewById(R.id.btFinLogin);
        assertNotNull(btLogin);
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}