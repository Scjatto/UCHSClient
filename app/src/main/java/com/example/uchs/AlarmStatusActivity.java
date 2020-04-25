package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AlarmStatusActivity extends AppCompatActivity {

    private TextView alarmMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_status);

        getSupportActionBar().setTitle("Community");
        Intent alarmStatusIntent = getIntent();
        Bundle alarmStatusBundle = alarmStatusIntent.getExtras();
        String alarmMessage = "There has been a " + alarmStatusBundle.getString("ALARM_TYPE");
        alarmMessage = alarmMessage + " by the user " + alarmStatusBundle.getString("USER_ID");
        alarmMessage = alarmMessage + " from the location " + alarmStatusBundle.getString("ALARM_LOCATION");

        alarmMsg = (TextView)findViewById(R.id.alarmDetails);
        alarmMsg.setText(alarmMessage);


    }
}
