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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigureSopActivity extends AppCompatActivity {

    public static final String TAG = "CONFIG_SOP";
    private Button gotToRaiseAlarm = null;

    private RelativeLayout updateGuardianLayout;
    private Button addNew;
    private Button checkUpdate;

    private int guardianViewId = 0;

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

        updateGuardianLayout = (RelativeLayout)findViewById(R.id.guardianLayout);
        addNew = (Button) findViewById(R.id.btInsertGuardian);
        checkUpdate = (Button)findViewById(R.id.btUpdateGuardians);

        RequestQueue requestQueue = Volley.newRequestQueue(ConfigureSopActivity.this);
        fetchUserGuardians(requestQueue, setTitle);

        gotToRaiseAlarm.setOnClickListener(skipToRaiseAlarm);
        addNew.setOnClickListener(setGuardian);
        checkUpdate.setOnClickListener(checkAndUpdateSOP);

        if (titleType.equals("help")) {
            gotToRaiseAlarm.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isRedundant() {
        for (int i = 0; i < updateGuardianLayout.getChildCount(); i++) {
            View view = updateGuardianLayout.getChildAt(i);

            if (view instanceof EditText) {
                String tempTxt = ((EditText) view).getText().toString();
                for (int j = i+1; j < updateGuardianLayout.getChildCount(); j++) {
                    View tempView = updateGuardianLayout.getChildAt(j);
                    if (tempView instanceof EditText) {
                        if (((EditText) tempView).getText().toString().equals(tempTxt) && !tempTxt.equals("")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void colorCode(String guardianString, String color) {
        for (int i=0; i<updateGuardianLayout.getChildCount(); i++) {
            View view = updateGuardianLayout.getChildAt(i);

            if (view instanceof EditText) {
                if(((EditText) view).getText().toString().equals(guardianString)) {
                    if (color.equals("red")) {
                        ((EditText) view).setTextColor(Color.RED);
                    }
                    if (color.equals("grey")) {
                        ((EditText) view).setTextColor(Color.GRAY);
                    }
                }
            }
        }
    }

    private void configureUpdatedSop(RequestQueue requestQueue, String guardians) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/configureSOP?";
        url += "uid=" + setTitle;
        url += "&guid_list=" + guardians;
        url += "&update=yes";
        Log.d(TAG,"URL: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        checkUpdate.setEnabled(true);
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                int check = response.getInt("check");
                                String msg = response.getString("desc");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                if (check == 1) {
                                    updateGuardianLayout.removeAllViews();
                                    RequestQueue newRequestQueue = Volley.newRequestQueue(ConfigureSopActivity.this);
                                    fetchUserGuardians(newRequestQueue,setTitle);
                                }
                            } else {
                                String msg = "Server Error!! Couldn't update SOP";
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
                checkUpdate.setEnabled(true);
                if (error instanceof NoConnectionError) {
                    msg = "No internet detected!! Please check the internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        checkUpdate.setEnabled(false);
        int socketTimeOut = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }

    private void checkIfUsersExist(final RequestQueue requestQueue, final String guardianString) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/clientExists?type=user";
        url += "&IDlist=" + guardianString;
        Log.d(TAG,"URL: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        checkUpdate.setEnabled(true);
                        try {
                            int status = response.getInt("status");
                            if(status == 1) {
                                int check = response.getInt("check");
                                if (check == 1) {
                                    // TODO Update and get guardians API Call
                                    for(int i=0;i<updateGuardianLayout.getChildCount();i++) {
                                        View view = updateGuardianLayout.getChildAt(i);
                                        if (view instanceof EditText) {
                                            ((EditText) view).setTextColor(Color.DKGRAY);
                                        }
                                    }
                                    Log.d(TAG, "Check Successful");
                                    RequestQueue updateRequestQueue = Volley.newRequestQueue(ConfigureSopActivity.this);
                                    configureUpdatedSop(updateRequestQueue, guardianString);
                                } else {
                                    JSONArray jsonArray = response.getJSONArray("invalid_IDs");
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        String tempStr = jsonArray.getString(i);
                                        colorCode(tempStr,"red");
                                    }
                                    JSONArray jsonArrayValid = response.getJSONArray("valid_IDs");
                                    for (int i=0; i<jsonArrayValid.length(); i++) {
                                        String tempStr = jsonArrayValid.getString(i);
                                        colorCode(tempStr, "grey");
                                    }
                                }
                            } else {
                                String msg = "Server Error!! Couldn't check existence of user";
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
                checkUpdate.setEnabled(true);
                if (error instanceof NoConnectionError) {
                    msg = "No internet detected!! Please check the internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        checkUpdate.setEnabled(false);
        int socketTimeOut = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 1, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }

    private void checkUserExists() {
        String guardiansToUpdate = "";
        int condCnt = 0;
        for (int i = 0; i < updateGuardianLayout.getChildCount(); i++) {
            View view = updateGuardianLayout.getChildAt(i);

            if(view instanceof EditText) {
                String tempString = ((EditText) view).getText().toString();
                if (!tempString.equals("")) {
                    if (condCnt == 0) {
                        guardiansToUpdate += tempString;
                    } else {
                        guardiansToUpdate += "," + tempString;
                    }
                    condCnt++;
                }
            }
        }
        Log.d(TAG,"Guardians To Update: " + guardiansToUpdate);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        checkIfUsersExist(requestQueue, guardiansToUpdate);
    }

    private View.OnClickListener checkAndUpdateSOP = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isRedundant()) {
                checkUserExists();
            } else {
                String msg = "Redundant fields present, please check again";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void fetchUserGuardians(final RequestQueue requestQueue, final String userID) {
        String url = "https://tribal-marker-274610.el.r.appspot.com/getGuardians?";
        url += "uid=" + userID;
        Log.d(TAG,"URL: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = "";
                            int status = response.getInt("status");
                            if (status == 1) {
                                int guardianNumber = response.getInt("#guardians");
                                guardianViewId = guardianNumber;
                                if (guardianNumber != 0) {
                                    JSONArray guardianList = response.getJSONArray("guardians");
                                    for (int i=0; i<guardianNumber; i++) {
                                        String guardianElement = guardianList.getString(i);
                                        createGuardianSection(updateGuardianLayout, guardianElement, i + 1);
                                    }
                                } else {
                                    msg = "No guardian for user " + userID + " has been configured";
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                msg = "Server Error!! Guardians could not be fetched";
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
                if (error instanceof NoConnectionError) {
                    msg = "No internet detected!! Please check the internet connection";
                } else {
                    msg = "Server Not Responding!!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeOut = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 2, 1);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);
    }


    private View.OnClickListener setGuardian = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (guardianViewId < 15) {
                createGuardianSection(updateGuardianLayout, "", ++ guardianViewId);
            } else {
                String msg = "Max limit of 15 guardians reached";
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void createGuardianSection(RelativeLayout layout, String content, int viewId) {
        createTextView(layout, content, viewId);
    }

    private void createTextView(RelativeLayout layout, String txtContent, int txtId) {
        EditText editText = new EditText(this);
        editText.setTextSize(22);
        editText.setId(txtId);
        editText.setHint("Enter guardian");
        editText.setText(txtContent);
        editText.setBackgroundResource(R.drawable.edit_text_border);
        editText.setPadding(25,10,25,5);
        Log.d(TAG,"TXT_ID: " + String.valueOf(txtId));

        RelativeLayout.LayoutParams editViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(editViewParams);
        if (txtId > 1) {
            editViewParams.addRule(RelativeLayout.BELOW, txtId - 1);
        }
        editViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        editViewParams.setMargins(0,15,0, 15);
        layout.addView(editText);
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
