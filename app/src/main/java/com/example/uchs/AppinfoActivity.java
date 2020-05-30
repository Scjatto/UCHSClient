package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AppinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        getSupportActionBar().setTitle("App Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
