package com.example.uchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Define variables for UI components
    private Button register = null;
    private Button login = null;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",MODE_PRIVATE);
        String accID = sharedPreferences.getString("ACC_ID","");
        String accPass = sharedPreferences.getString("ACC_PASS","");
        String accType = sharedPreferences.getString("ACC_TYPE","");

        if(!accID.equals("") && !accPass.equals("") && !accType.equals("")) {

            Intent serviceIntent = new Intent(MainActivity.this, PollAlert.class);
            Bundle serviceBundle = new Bundle();
            serviceBundle.putString("AccountID", accID);
            serviceBundle.putString("IDType", accType);
            serviceIntent.putExtras(serviceBundle);
            startService(serviceIntent);

            if (accType.equals("user")) {
                Intent intent = new Intent(this, RaiseAlarmActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("RAISE_ALARM_ID", accID);
                dataBundle.putString("ID_TYPE", accType);
                intent.putExtras(dataBundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ConfigureSopActivity.class);
                Bundle dataBundle = new Bundle();
                dataBundle.putString("CONFIGURE_SOP_TITLE",accID);
                dataBundle.putString("USER_TYPE",accType);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        }



        // UI components assigned to class variables
        register = (Button)findViewById(R.id.btRegister);
        login = (Button)findViewById(R.id.btLogin);

        // Assign Listeners to Buttons
        register.setOnClickListener(regListener);
        login.setOnClickListener(loginListener);

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

    private View.OnClickListener regListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String regTxt = "Clicked " + register.getText().toString();
            System.out.println(regTxt);

            Intent regIntent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(regIntent);
            finish();
        }
    };

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String loginTxt = "Clicked " + login.getText().toString();
            System.out.println(loginTxt);

            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.app_info:
                Intent intent = new Intent(MainActivity.this, AppinfoActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

