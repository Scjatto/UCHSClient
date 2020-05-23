package com.example.uchs;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
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
    private boolean pollStopStatus = false;
    private String accountID;
    private String accountType;

    public static final String DEBUG_TAG = "PollAlert";

    public PollAlert() {
        super("PollAlert");
        setIntentRedelivery(true);
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
//        startTimer();
//        String msg = "Timer Started with timeVal: " + String.valueOf(timeVal);
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }


    public void fetchAlert(RequestQueue requestQueue, String accountID, String accountType) {
//        String url = "https://jsonplaceholder.typicode.com/todos/1";
        String url = "https://tribal-marker-274610.el.r.appspot.com/";
        if (accountType.toLowerCase().equals("user")) {
//            url = "https://tribal-marker-274610.el.r.appspot.com/checkUserAlerts?";
            url += "checkUserAlerts?uid=" + accountID;
        }
        else if (accountType.toLowerCase().equals("help")) {
            url += "checkHelplineAlerts?hid=" + accountID;
        }


        url = String.format(url);
        System.out.println(url);
        Log.d(DEBUG_TAG, url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONArray alertDetails = response.getJSONArray("alarmDetails");
                                if (alertDetails.length() != 0) {
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
                                        Log.d(DEBUG_TAG, msg);
                                    }
                                } else {
                                    System.out.println("No alert found");
                                    Log.d(DEBUG_TAG, "No alert found");
                                }

                            }
                            else {
                                System.out.println("Something Went Wrong!!");
                                Log.d(DEBUG_TAG, "Something Went Wrong!!");
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
                String msg;
                if (error instanceof NoConnectionError) {
                    msg = "No Internet detected. Please check your internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_TAG, " Server Not Responding:: " + error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        assert intent != null;
        Bundle dataBundle = intent.getExtras();
        assert dataBundle != null;
        accountID = dataBundle.getString("AccountID");
        accountType = dataBundle.getString("IDType");

        while (true) {
            // TODO Notification Builder {Initial Testing}
            // NOW Simulating timer
//            incrementTimer();
//            System.out.println(accountID + ":: Time Now: " + String.valueOf(timeVal));
//            Log.d(DEBUG_TAG, accountID + ":: Time Now: " + String.valueOf(timeVal));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            fetchAlert(requestQueue, accountID, accountType);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pollStopStatus) {
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
//        String msg = "Timer stopped with timeVal: " + String.valueOf(timeVal);
//        endTimer();
//        msg += "Time reset to : " + String.valueOf(timeVal);
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        pollStopStatus = true;
//        Toast.makeText(getApplicationContext(),"API Polling Stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
