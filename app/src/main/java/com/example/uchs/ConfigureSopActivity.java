package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfigureSopActivity extends AppCompatActivity {

    private TextView gotToRaiseAlarm = null;

    private String setTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_sop);

        Intent getIntent = getIntent();
        Bundle dataExtras = getIntent.getExtras();
        setTitle = dataExtras.getString("CONFIGURE_SOP_TITLE");

        getSupportActionBar().setTitle(setTitle);

        gotToRaiseAlarm = (TextView)findViewById(R.id.skipToAlarm);
        gotToRaiseAlarm.setOnClickListener(skipToRaiseAlarm);
    }

    private View.OnClickListener skipToRaiseAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent raiseAlarmIntent = new Intent(ConfigureSopActivity.this,RaiseAlarmActivity.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putString("RAISE_ALARM_ID",setTitle);

            raiseAlarmIntent.putExtras(dataBundle);
            startActivity(raiseAlarmIntent);
        }
    };
}
