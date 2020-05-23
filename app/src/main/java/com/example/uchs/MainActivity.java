package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials",MODE_PRIVATE);
        String accID = sharedPreferences.getString("ACC_ID","");
        String accPass = sharedPreferences.getString("ACC_PASS","");
        String accType = sharedPreferences.getString("ACC_TYPE","");

        if(!accID.equals("") && !accPass.equals("") && !accType.equals("")) {
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

}
