package com.example.uchs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ConfigureSopActivity extends AppCompatActivity {

    private Button gotToRaiseAlarm = null;

    private String setTitle = null;
    private String titleType = null;

    private boolean pollStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_sop);

        Intent getIntent = getIntent();
        Bundle dataExtras = getIntent.getExtras();
        setTitle = dataExtras.getString("CONFIGURE_SOP_TITLE");
        titleType = dataExtras.getString("USER_TYPE");

        getSupportActionBar().setTitle(setTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!pollStatus) {
            Intent intent = new Intent(ConfigureSopActivity.this,PollAlert.class);
            Bundle serviceBundle = new Bundle();
            serviceBundle.putString("AccountID",setTitle);
            serviceBundle.putString("IDType",titleType);

            intent.putExtras(serviceBundle);
            startService(intent);
            pollStatus = true;
        }

        gotToRaiseAlarm = (Button) findViewById(R.id.button_skip);
        gotToRaiseAlarm.setOnClickListener(skipToRaiseAlarm);

        if (titleType.equals("help")) {
            gotToRaiseAlarm.setVisibility(View.INVISIBLE);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                logoutdialogue();
                break;
            case R.id.app_info:
                Intent intent = new Intent(ConfigureSopActivity.this, AppinfoActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutFetchApi(RequestQueue requestQueue) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/logout?";
        url += "type=" + titleType;
        url += "&id=" + setTitle;
        Log.d("ConfigureSopLogout",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                int check = response.getInt("check");
                                if (check == 1) {
                                    String msg = response.getString("desc");
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                    logoutmethod();
                                } else {
                                    String msg = response.getString("desc");
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String msg = "Server Error!! Could not logout";
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        });
        int socketTimeOut = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }

    public void logoutdialogue(){
        AlertDialog.Builder logdial = new AlertDialog.Builder(ConfigureSopActivity.this);
        logdial.setMessage("Are you sure you want to logout ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue newRequestQueue = Volley.newRequestQueue(ConfigureSopActivity.this);
                        logoutFetchApi(newRequestQueue);
//                        logoutmethod();
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
        Intent logoutIntent = new Intent(ConfigureSopActivity.this,MainActivity.class);
        Intent serviceStopIntent = new Intent(ConfigureSopActivity.this,PollAlert.class);
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private View.OnClickListener skipToRaiseAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent raiseAlarmIntent = new Intent(ConfigureSopActivity.this,RaiseAlarmActivity.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putString("RAISE_ALARM_ID",setTitle);
            dataBundle.putString("ID_TYPE",titleType);

            raiseAlarmIntent.putExtras(dataBundle);
            startActivity(raiseAlarmIntent);
        }
    };

    @Override
    protected void onDestroy() {
        pollStatus = true;
        super.onDestroy();
    }
}
