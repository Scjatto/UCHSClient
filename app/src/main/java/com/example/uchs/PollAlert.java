package com.example.uchs;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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


    public void fetchAlert(RequestQueue requestQueue) {
        String url = "https://jsonplaceholder.typicode.com/todos/1";
        url = String.format(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String data = response.toString();
                        System.out.println("Response from server >>>>>>>: " + data);
                        // TODO Notification Builder with alert message
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        assert intent != null;
        Bundle dataBundle = intent.getExtras();
        assert dataBundle != null;
        String accountID = dataBundle.getString("AccountID");

        while (true) {
            // TODO Poll API {Later}
            // TODO Notification Builder {Initial Testing}
            // NOW Simulating timer
            incrementTimer();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            fetchAlert(requestQueue);
            if (timeVal % 5 == 0) {
                System.out.println(accountID + ":: Time Now: " + String.valueOf(timeVal));
            }
            try {
                Thread.sleep(5000);
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
