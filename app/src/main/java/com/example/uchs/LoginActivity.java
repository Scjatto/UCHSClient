package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivity extends AppCompatActivity {

    Spinner spinner_login_cat;
    ArrayAdapter<CharSequence> adapter_login_cat;

    private EditText idphone;
    private EditText password;
    private Button finLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("UCHSLogin");

        idphone = (EditText)findViewById(R.id.editIdPhone);
        password = (EditText)findViewById(R.id.editPassword);
        finLogin = (Button)findViewById(R.id.btFinLogin);

        idphone.addTextChangedListener(contentEnterWatcher);
        password.addTextChangedListener(contentEnterWatcher);
        finLogin.setOnClickListener(finLoginListener);

        spinner_login_cat = (Spinner) findViewById(R.id.login_edit_category);
        adapter_login_cat = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter_login_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_login_cat.setAdapter(adapter_login_cat);

    }

    private TextWatcher contentEnterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String idPhoneInput = idphone.getText().toString();
            String passwdInput = password.getText().toString();

            finLogin.setEnabled(!idPhoneInput.isEmpty() && !passwdInput.isEmpty());
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
