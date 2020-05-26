package com.example.uchs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        String alarmMessage = alarmStatusBundle.getString("ACK");

        alarmMsg = (TextView)findViewById(R.id.alarmDetails);
        alarmMsg.setText(alarmMessage);


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
        MenuItem item1 = menu.findItem(R.id.logout);
        item1.setVisible(false);

        return true;
    }
}
