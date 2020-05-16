package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    Spinner spinner_prof;
    ArrayAdapter<CharSequence> adapter_prof;
    ArrayAdapter<CharSequence> adapter_serv;
    Spinner spinner_cat;
    ArrayAdapter<CharSequence> adapter_cat;
    Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner_prof = (Spinner) findViewById(R.id.edit_Profession);
        adapter_prof = ArrayAdapter.createFromResource(this, R.array.professions, android.R.layout.simple_spinner_item);
        adapter_prof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_serv = ArrayAdapter.createFromResource(this, R.array.Services, android.R.layout.simple_spinner_item);
        adapter_serv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_cat = (Spinner) findViewById(R.id.edit_Category);
        adapter_cat = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapter_cat);

        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView prof = (TextView) findViewById(R.id.enter_Profession);
                if (position==0) {
                    prof.setText("Profession");
                    spinner_prof.setAdapter(adapter_prof);
                }
                else if(position==1){
                    prof.setText("Service Type");
                    spinner_prof.setAdapter(adapter_serv);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getSupportActionBar().setTitle("UCHSRegister");
    }
}
