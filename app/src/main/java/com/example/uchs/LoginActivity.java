package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText idphone;
    private Button finLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("UCHSLogin");

        idphone = (EditText)findViewById(R.id.editIdPhone);
        finLogin = (Button)findViewById(R.id.btFinLogin);

        idphone.addTextChangedListener(idPhoneWatcher);
        finLogin.setOnClickListener(finLoginListener);

    }

    private TextWatcher idPhoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String idPhoneInput = idphone.getText().toString();

            finLogin.setEnabled(!idPhoneInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private View.OnClickListener finLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // TODO Check ID existing in the server

            String id = idphone.getText().toString(); // TODO Changed to only UID when server entry is matched

            Intent finLoginIntent = new Intent(LoginActivity.this,ConfigureSopActivity.class);
            Bundle dataExtra = new Bundle();
            dataExtra.putString("CONFIGURE_SOP_TITLE",id);


            finLoginIntent.putExtras(dataExtra);
            startActivity(finLoginIntent);
            finish();



        }
    };
}
