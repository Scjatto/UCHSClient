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

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setActionBar();
        onNewIntent(getIntent());
    }

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg));
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
        MenuItem item1 = menu.findItem(R.id.logout);
        item1.setVisible(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println(("New notification instance created"));
        String msg = intent.getStringExtra("message") ;
        System.out.println("Message : "+msg);
        TextView tvNotify = findViewById(R.id.notification_view) ;
        tvNotify.setText(msg) ;
    }
}
