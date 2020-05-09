package com.example.uchs;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PollAlert extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private int timeVal;
    public PollAlert() {
        super("Poll_Alert");
    }

    private void startTimer() {
        timeVal = 0;
    }

    private void endTimer() {
        timeVal = 0;
    }

    private void incrementTimer() {
        timeVal += 1;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        startTimer();
        String msg = "Timer Started with timeVal: " + String.valueOf(timeVal);
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (true) {
            // TODO Poll API {Later}
            // TODO Notification Builder {Initial Testing}
            // NOW Simulating timer
            incrementTimer();
            if (timeVal % 5 == 0) {
                System.out.println("Time Now: " + String.valueOf(timeVal));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        String msg = "Timer stopped with timeVal: " + String.valueOf(timeVal);
        endTimer();
        msg += "Time reset to : " + String.valueOf(timeVal);
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        stopSelf();
        super.onDestroy();
    }
}
