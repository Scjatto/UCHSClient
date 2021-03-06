package com.example.uchs;

import androidx.annotation.NonNull;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.app_info:
                Intent intent = new Intent(AlarmStatusActivity.this, AppinfoActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
