package com.example.uchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfigureSopActivity extends AppCompatActivity {

    private TextView gotToRaiseAlarm = null;
    private Button logout;

    private String setTitle = null;
    private String titleType = null;

    private boolean pollStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_sop);

        Intent getIntent = getIntent();
        Bundle dataExtras = getIntent.getExtras();
        setTitle = dataExtras.getString("CONFIGURE_SOP_TITLE");
        titleType = dataExtras.getString("USER_TYPE");

        getSupportActionBar().setTitle(setTitle);

        if(!pollStatus) {
            Intent intent = new Intent(ConfigureSopActivity.this,PollAlert.class);
            Bundle serviceBundle = new Bundle();
            serviceBundle.putString("AccountID",setTitle);
            serviceBundle.putString("IDType",titleType);

            intent.putExtras(serviceBundle);
            startService(intent);
            pollStatus = true;
        }

        gotToRaiseAlarm = (TextView)findViewById(R.id.skipToAlarm);
        logout = (Button)findViewById(R.id.btLogout);

        gotToRaiseAlarm.setOnClickListener(skipToRaiseAlarm);
        logout.setOnClickListener(logOut);

        if (titleType.equals("help")) {
            gotToRaiseAlarm.setVisibility(View.INVISIBLE);
        }
    }

    private View.OnClickListener logOut = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent logoutIntent = new Intent(ConfigureSopActivity.this,LoginActivity.class);
            Intent serviceStopIntent = new Intent(ConfigureSopActivity.this,PollAlert.class);
            SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("ACC_ID", "");
//            editor.putString("ACC_PASS", "");
//            editor.putString("ACC_TYPE", "");
            editor.clear();
            editor.apply();
            pollStatus = false;
            stopService(serviceStopIntent);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private View.OnClickListener skipToRaiseAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent raiseAlarmIntent = new Intent(ConfigureSopActivity.this,RaiseAlarmActivity.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putString("RAISE_ALARM_ID",setTitle);
            dataBundle.putString("ID_TYPE",titleType);

            raiseAlarmIntent.putExtras(dataBundle);
            startActivity(raiseAlarmIntent);
        }
    };

    @Override
    protected void onDestroy() {
        pollStatus = true;
        super.onDestroy();
    }
}
