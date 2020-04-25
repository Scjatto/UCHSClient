package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RaiseAlarmActivity extends AppCompatActivity {

    private Button raise;
    private TextView raiseLabel;
    private Button medical;
    private Button fire;
    private Button lost;
    private String setID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);

        setID = getIntent().getStringExtra("RAISE_ALARM_ID");
        getSupportActionBar().setTitle(setID);

        raise = (Button)findViewById(R.id.btRaiseAlarm);
        raiseLabel = (TextView)findViewById(R.id.txtRaiseAlarm);
        medical = (Button)findViewById(R.id.btMedical);
        fire = (Button)findViewById(R.id.btFire);
        lost = (Button)findViewById(R.id.btLost);

        raise.setOnClickListener(raiseAlarmListener);
        medical.setOnClickListener(triggerAlarmListener);
        fire.setOnClickListener(triggerAlarmListener);
        lost.setOnClickListener(triggerAlarmListener);
    }

    @Override
    public void onBackPressed() {

        if (raise.getVisibility() == View.INVISIBLE && raiseLabel.getVisibility() == View.VISIBLE) {

            raiseLabel.setVisibility(View.INVISIBLE);
            medical.setVisibility(View.INVISIBLE);
            fire.setVisibility(View.INVISIBLE);
            lost.setVisibility(View.INVISIBLE);
            raise.setVisibility(View.VISIBLE);

        } else {
            super.onBackPressed();
        }
    }

    private View.OnClickListener raiseAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            raise.setVisibility(View.INVISIBLE);
            raiseLabel.setVisibility(View.VISIBLE);
            medical.setVisibility(View.VISIBLE);
            fire.setVisibility(View.VISIBLE);
            lost.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener triggerAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button tempBt = (Button)findViewById(v.getId());
            System.out.println(tempBt.getText().toString());

            Location triggerLocation = new Location(); // TODO Location API call {Async Thread}
            Location alarmLocation = triggerLocation.fetchLocation();
            String strAlarmLocation = "Lat: " + String.valueOf(alarmLocation.latitude) + " Long: " + String.valueOf(alarmLocation.longitude);
            String userID = setID;
            String alarmType = tempBt.getText().toString();
            Alarm alarm = new Alarm(userID, alarmType, alarmLocation);
            // TODO Send alarm to Server Endpoint {NEED async task extension of Alarm class}
            Intent alarmIntent = new Intent(RaiseAlarmActivity.this,AlarmStatusActivity.class);
            Bundle alarmBundle = new Bundle();
            alarmBundle.putString("USER_ID",userID);
            alarmBundle.putString("ALARM_TYPE",alarmType);
            alarmBundle.putString("ALARM_LOCATION",strAlarmLocation);
            alarmIntent.putExtras(alarmBundle);
            startActivity(alarmIntent);

        }
    };

}
