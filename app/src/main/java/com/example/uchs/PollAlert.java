package com.example.uchs;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.NoConnectionError;
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
    private boolean pollStopStatus = false;
    private String accountID;
    private String accountType;

    public static final String DEBUG_TAG = "PollAlert";
    public static final String CHANNEL_ID = "channel1";
    private NotificationManagerCompat notificationManager;

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
                                        String onNotifMsg = alert.genAlertMsg();
                                        String expanded_msg = alert.genExpandedMsg();
                                        String detailedMsg = alert.genDetailedMsg();
                                        Log.d(DEBUG_TAG, onNotifMsg);
                                        Log.d(DEBUG_TAG, detailedMsg);
                                        createNotificationChannel();
                                        notificationManager = NotificationManagerCompat.from(PollAlert.this);
                                        int not_id = (int) System.currentTimeMillis();
                                        sendNotification(not_id, onNotifMsg, expanded_msg, detailedMsg);
                                        Log.d(DEBUG_TAG, "Notification sent");
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
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Log.d(DEBUG_TAG, msg + ": " + error.getMessage());
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

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            fetchAlert(requestQueue, accountID, accountType);
            try {
                Thread.sleep(30000);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alert Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is Alert Channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(int not_id, String onNotifmsg, String expanded_msg, String detailedMessage) {
        String title = "Emergency Alert";

        Intent intent_not = new Intent(this, NotificationActivity.class);
        intent_not.putExtra("message", detailedMessage);
        intent_not.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, not_id, intent_not,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_alert)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_round_org_size))
                .setContentTitle(title)
                .setContentText(onNotifmsg)
                .setColor(getResources().getColor(R.color.red))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(expanded_msg))
                .build();
        notificationManager.notify(not_id, notification);
    }
}
