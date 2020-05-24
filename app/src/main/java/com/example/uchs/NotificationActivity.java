package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println(("New notification instance created"));
        String msg = intent.getStringExtra("message") ;
        String id = intent.getStringExtra("id") ;
        System.out.println("Message : "+msg);
        System.out.println("Notification id : "+id);
        TextView tvNotify = findViewById(R.id.notification_view) ;
        tvNotify.setText(msg+id) ;
    }
}
