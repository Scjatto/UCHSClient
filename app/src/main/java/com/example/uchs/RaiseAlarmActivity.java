package com.example.uchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RaiseAlarmActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private ImageButton raise;
    private TextView raiseLabel;
    private ImageButton medical;
    private ImageButton fire;
    private ImageButton lost;
    private ProgressBar reqProgress;
    private TextView progressTxt;

    private String setID;
    private String idType;
    private RequestQueue requestQueue;

    private boolean pollStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);

        setID = getIntent().getStringExtra("RAISE_ALARM_ID");
        idType = getIntent().getStringExtra("ID_TYPE");
        getSupportActionBar().setTitle(setID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        layout = (ConstraintLayout) findViewById(R.id.raiseAlarmLayout);
        raise = (ImageButton)findViewById(R.id.btRaiseAlarm);
        raiseLabel = (TextView)findViewById(R.id.txtRaiseAlarm);
        medical = (ImageButton) findViewById(R.id.Medical);
        fire = (ImageButton) findViewById(R.id.Fire);
        lost = (ImageButton) findViewById(R.id.LostFound);
        reqProgress = (ProgressBar)findViewById(R.id.requestProgress);
        progressTxt = (TextView)findViewById(R.id.progressText);

        raise.setOnClickListener(raiseAlarmListener);
        medical.setOnClickListener(triggerAlarmListener);
        fire.setOnClickListener(triggerAlarmListener);
        lost.setOnClickListener(triggerAlarmListener);
    }


    @SuppressLint("RestrictedApi")
    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                logoutdialogue();
                break;
            case  R.id.configure_sop:
                configuresopnav();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutdialogue(){
        AlertDialog.Builder logdial = new AlertDialog.Builder(RaiseAlarmActivity.this);
        logdial.setMessage("Are you sure you want to logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutmethod();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog logalert = logdial.create();
        logalert.setTitle("Logout");
        logalert.show();
    }

    private void logoutmethod() {
        Intent logoutIntent = new Intent(RaiseAlarmActivity.this,MainActivity.class);
        Intent serviceStopIntent = new Intent(RaiseAlarmActivity.this,PollAlert.class);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginCredentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        pollStatus = false;
        stopService(serviceStopIntent);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }


    public void configuresopnav() {
        Intent intent = new Intent(RaiseAlarmActivity.this,ConfigureSopActivity.class);
        Bundle dataBundle = new Bundle();
        dataBundle.putString("CONFIGURE_SOP_TITLE",setID);
        dataBundle.putString("USER_TYPE",idType);
        intent.putExtras(dataBundle);
        startActivity(intent);
    };

    @Override
    public void onBackPressed() {

        if (raise.getVisibility() == View.INVISIBLE && raiseLabel.getVisibility() == View.VISIBLE) {

            medical.setVisibility(View.INVISIBLE);
            fire.setVisibility(View.INVISIBLE);
            lost.setVisibility(View.INVISIBLE);
            raise.setVisibility(View.VISIBLE);
        } else {
//            super.onBackPressed();
            moveTaskToBack(true);
        }

    }

    private View.OnClickListener raiseAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            raise.setVisibility(View.INVISIBLE);
            raiseLabel.setVisibility(View.VISIBLE);
            medical.setVisibility(View.VISIBLE);
            fire.setVisibility(View.VISIBLE);
            lost.setVisibility(View.VISIBLE);
        }
    };

    private void buttonStatus(boolean btStatus) {
        for(int i=0; i< layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);

            if (view instanceof Button) {
                view.setEnabled(btStatus);
            }
        }
    };

    private void sendAlarmToServer(final Alarm alarm) {
        String uID = alarm.userID;
//        String alarmID = alarm.alarmID;
        final String alarmType = alarm.alarmType.replace(" ","");
        final Location alarmLocation = alarm.alarmLocation;
        final String strLocation = String.valueOf(alarmLocation.latitude) + "," + String.valueOf(alarmLocation.longitude);
        final String alarmTimeStamp = alarm.alarmTimeStamp;
        String initUrl = "https://tribal-marker-274610.el.r.appspot.com/raiseAlarm?";
        initUrl = initUrl + "userID=" + uID;
        initUrl = initUrl + "&alarmType=" + alarmType;
//        initUrl = initUrl + "&alarmTS=" + alarmTimeStamp;
        initUrl = initUrl + "&alarmLoc=" + strLocation;
        String url = String.format(initUrl);
//        System.out.println(url);
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        reqProgress.setVisibility(View.GONE);
                        progressTxt.setVisibility(View.GONE);
                        buttonStatus(true);
                        try {
                            int responseStatus = response.getInt("status");
                            if (responseStatus == 1) {
                                JSONObject data = response.getJSONObject("data");
//                            System.out.println(data);
                                String serverResponse = data.getString("ACK");
                                serverResponse = serverResponse.replace(strLocation, alarmLocation.locName);
                                serverResponse += "\nAlarmID=" + data.getString("ID");
//                                serverResponse = serverResponse.replace(alarmTimeStamp , alarm.humanReadableTimeStamp(alarmTimeStamp));
//                            System.out.println(serverResponse.replace(alarmType, alarm.alarmType));
                                Intent alarmIntent = new Intent(RaiseAlarmActivity.this, AlarmStatusActivity.class);
                                Bundle alarmBundle = new Bundle();
                                alarmBundle.putString("ACK", serverResponse.replace(alarmType, alarm.alarmType));
                                alarmIntent.putExtras(alarmBundle);
                                startActivity(alarmIntent);
                            } else {
                                String errorStr = "Server Error: Failed to raise Alarm";
                                Toast.makeText(getApplicationContext(),errorStr,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        reqProgress.setVisibility(View.GONE);
                        progressTxt.setVisibility(View.GONE);
                        buttonStatus(true);
                        String msg;
                        if (error instanceof TimeoutError) {
                            msg = "Request Timed Out!! Please try again!!";
                        } else if (error instanceof NoConnectionError) {
                            msg = "No internet detected!! Please check the internet connection";
                        } else {
                            msg = "Server Not Responding!!";
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } );
        reqProgress.setVisibility(View.VISIBLE);
        reqProgress.bringToFront();
        progressTxt.setText("Raising Alarm ...");
        progressTxt.setVisibility(View.VISIBLE);
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonReq.setRetryPolicy(policy);
        requestQueue.add(jsonReq);
    };

    private View.OnClickListener triggerAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton tempBt = (ImageButton) findViewById(v.getId());
            buttonStatus(false);

            Location triggerLocation = new Location(); // TODO Location API call {Async Thread}
            Location alarmLocation = triggerLocation.fetchLocation();
            String strAlarmLocation = "Lat: " + String.valueOf(alarmLocation.latitude) + " Long: " + String.valueOf(alarmLocation.longitude);
            String userID = setID;
            String[] arrstr = v.getResources().getResourceName(v.getId()).split("/",2);
            String alarmType = arrstr[arrstr.length-1];
            Alarm alarm = new Alarm(userID, alarmType, alarmLocation);
            // TODO Send alarm to Server Endpoint {NEED async task extension of Alarm class}
            sendAlarmToServer(alarm);

        }
    };

    @Override
    protected void onDestroy() {
        pollStatus=true;
        super.onDestroy();
    }
}
