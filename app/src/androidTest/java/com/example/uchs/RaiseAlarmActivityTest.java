package com.example.uchs;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class RaiseAlarmActivityTest {

    @Rule
    public ActivityTestRule<RaiseAlarmActivity> raiseActivityTestRule = new ActivityTestRule<RaiseAlarmActivity>(RaiseAlarmActivity.class);
    private RaiseAlarmActivity raiseAlarmActivity = null;

    @Before
    public void setUp() throws Exception {
        raiseAlarmActivity = raiseActivityTestRule.getActivity();
    }

    @Test
    public void launchRaiseAlarm() {
        int btRaiseAlarm = raiseAlarmActivity.findViewById(R.id.btRaiseAlarm).getVisibility();
        int btMedical = raiseAlarmActivity.findViewById(R.id.btMedical).getVisibility();
        int btFire = raiseAlarmActivity.findViewById(R.id.btFire).getVisibility();
        int btLost = raiseAlarmActivity.findViewById(R.id.btLost).getVisibility();
        assertEquals(btRaiseAlarm,View.VISIBLE);
        assertEquals(btMedical,View.INVISIBLE);
        assertEquals(btFire,View.INVISIBLE);
        assertEquals(btLost,View.INVISIBLE);

    }

    @Test
    public void pressRaiseAlarmButton() {
        onView(withId(R.id.btRaiseAlarm)).perform(click());
        int btRaiseAlarm = raiseAlarmActivity.findViewById(R.id.btRaiseAlarm).getVisibility();
        int btMedical = raiseAlarmActivity.findViewById(R.id.btMedical).getVisibility();
        int btFire = raiseAlarmActivity.findViewById(R.id.btFire).getVisibility();
        int btLost = raiseAlarmActivity.findViewById(R.id.btLost).getVisibility();
        assertEquals(btRaiseAlarm,View.INVISIBLE);
        assertEquals(btMedical,View.VISIBLE);
        assertEquals(btFire,View.VISIBLE);
        assertEquals(btLost,View.VISIBLE);
    }

    @Test
    public void pressBackAfterRaiseAlarmPressed(){
        onView(withId(R.id.btRaiseAlarm)).perform(click());
        Espresso.pressBack();
        int btRaiseAlarm = raiseAlarmActivity.findViewById(R.id.btRaiseAlarm).getVisibility();
        int btMedical = raiseAlarmActivity.findViewById(R.id.btMedical).getVisibility();
        int btFire = raiseAlarmActivity.findViewById(R.id.btFire).getVisibility();
        int btLost = raiseAlarmActivity.findViewById(R.id.btLost).getVisibility();
        assertEquals(btRaiseAlarm,View.VISIBLE);
        assertEquals(btMedical,View.INVISIBLE);
        assertEquals(btFire,View.INVISIBLE);
        assertEquals(btLost,View.INVISIBLE);
    }

    @After
    public void tearDown() throws Exception {
        raiseAlarmActivity = null;
    }
}