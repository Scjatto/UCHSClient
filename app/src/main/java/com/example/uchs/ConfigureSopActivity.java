package com.example.uchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfigureSopActivity extends AppCompatActivity {

    private Button gotToRaiseAlarm = null;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!pollStatus) {
            Intent intent = new Intent(ConfigureSopActivity.this,PollAlert.class);
            Bundle serviceBundle = new Bundle();
            serviceBundle.putString("AccountID",setTitle);
            serviceBundle.putString("IDType",titleType);

            intent.putExtras(serviceBundle);
            startService(intent);
            pollStatus = true;
        }

        gotToRaiseAlarm = (Button) findViewById(R.id.button_skip);
        gotToRaiseAlarm.setOnClickListener(skipToRaiseAlarm);

        if (titleType.equals("help")) {
            gotToRaiseAlarm.setVisibility(View.INVISIBLE);
        }
    }


    @SuppressLint("RestrictedApi")
    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        MenuItem item = menu.findItem(R.id.configure_sop);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                logoutdialogue();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutdialogue(){
        AlertDialog.Builder logdial = new AlertDialog.Builder(ConfigureSopActivity.this);
        logdial.setMessage("Are you sure you want to logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutmethod();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog logalert = logdial.create();
        logalert.setTitle("Logout");
        logalert.show();
    }

    private void logoutmethod() {
        Intent logoutIntent = new Intent(ConfigureSopActivity.this,MainActivity.class);
        Intent serviceStopIntent = new Intent(ConfigureSopActivity.this,PollAlert.class);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        pollStatus = false;
        stopService(serviceStopIntent);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }

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
