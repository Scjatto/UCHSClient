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

import org.json.JSONArray;
import org.json.JSONException;
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


    public void fetchAlert(RequestQueue requestQueue, String accountID, String accountType) {
//        String url = "https://jsonplaceholder.typicode.com/todos/1";
        String url = "https://tribal-marker-274610.el.r.appspot.com/";
        if (accountType.toLowerCase().equals("user")) {
//            url = "https://tribal-marker-274610.el.r.appspot.com/checkUserAlerts?";
            url += "checkUserAlerts?uid=" + accountID;
        }
        else if (accountType.toLowerCase().equals("helpline")) {
            url += "checkHelplineAlerts?hid=" + accountID;
        }


        url = String.format(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONArray alertDetails = response.getJSONArray("alarmDetails");
                                for (int itr = 0; itr < alertDetails.length(); itr ++) {
                                    JSONObject eachAlert = alertDetails.getJSONObject(itr);

                                    String alarmType = eachAlert.getString("atype");
                                    double alarmLat = eachAlert.getDouble("lat");
                                    double alarmLon = eachAlert.getDouble("lon");
                                    long alarmTS = eachAlert.getLong("tstamp");
                                    String alarmUser = eachAlert.getString("user");

                                    Alert alert = new Alert(alarmType, alarmLat, alarmLon, alarmTS, alarmUser);
                                    String msg = alert.genAlertMsg();
                                    System.out.println(msg);
                                }
                            }
                            else {
                                System.out.println("Something Went Wrong!!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        System.out.println("Response from server >>>>>>>: " + data);
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
        String accountType = dataBundle.getString("IDType");

        while (true) {
            // TODO Poll API {Later}
            // TODO Notification Builder {Initial Testing}
            // NOW Simulating timer
            incrementTimer();
            System.out.println(accountID + ":: Time Now: " + String.valueOf(timeVal));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            fetchAlert(requestQueue, accountID, accountType);

            try {
                Thread.sleep(10000);
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
