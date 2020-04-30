package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RaiseAlarmActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private Button raise;
    private TextView raiseLabel;
    private Button medical;
    private Button fire;
    private Button lost;
    private ProgressBar reqProgress;
    private TextView progressTxt;

    private String setID;
    private RequestQueue requestQueue;
//    private String serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);

        setID = getIntent().getStringExtra("RAISE_ALARM_ID");
        getSupportActionBar().setTitle(setID);

        requestQueue = Volley.newRequestQueue(this);

        layout = (ConstraintLayout) findViewById(R.id.raiseAlarmLayout);
        raise = (Button)findViewById(R.id.btRaiseAlarm);
        raiseLabel = (TextView)findViewById(R.id.txtRaiseAlarm);
        medical = (Button)findViewById(R.id.btMedical);
        fire = (Button)findViewById(R.id.btFire);
        lost = (Button)findViewById(R.id.btLost);
        reqProgress = (ProgressBar)findViewById(R.id.requestProgress);
        progressTxt = (TextView)findViewById(R.id.progressText);

        raise.setOnClickListener(raiseAlarmListener);
        medical.setOnClickListener(triggerAlarmListener);
        fire.setOnClickListener(triggerAlarmListener);
        lost.setOnClickListener(triggerAlarmListener);
    }

    @Override
    public void onBackPressed() {

        if (raise.getVisibility() == View.INVISIBLE && raiseLabel.getVisibility() == View.VISIBLE) {

            raiseLabel.setVisibility(View.INVISIBLE);
            medical.setVisibility(View.INVISIBLE);
            fire.setVisibility(View.INVISIBLE);
            lost.setVisibility(View.INVISIBLE);
            raise.setVisibility(View.VISIBLE);

        } else {
            super.onBackPressed();
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
        String alarmID = alarm.alarmID;
        final String alarmType = alarm.alarmType.replace(" ","");
        final Location alarmLocation = alarm.alarmLocation;
        final String strLocation = String.valueOf(alarmLocation.latitude) + "," + String.valueOf(alarmLocation.longitude);
        final String alarmTimeStamp = alarm.alarmTimeStamp;
        String initUrl = "https://tribal-marker-274610.el.r.appspot.com/raiseAlarm?";
        initUrl = initUrl + "userID=" + uID;
        initUrl = initUrl + "&alarmID=" + alarmID;
        initUrl = initUrl + "&alarmType=" + alarmType;
        initUrl = initUrl + "&alarmTS=" + alarmTimeStamp;
        initUrl = initUrl + "&alarmLoc=" + strLocation;
        String url = String.format(initUrl);
//        System.out.println(url);
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reqProgress.setVisibility(View.GONE);
                            progressTxt.setVisibility(View.GONE);
                            buttonStatus(true);
                            int responseStatus = response.getInt("status");
                            if (responseStatus == 1) {
                                JSONObject data = response.getJSONObject("data");
//                            System.out.println(data);
                                String serverResponse = data.getString("ACK");
                                serverResponse = serverResponse.replace(strLocation, alarmLocation.getLocationName());
                                serverResponse = serverResponse.replace(alarmTimeStamp , alarm.humanReadableTimeStamp(alarmTimeStamp));
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
                            reqProgress.setVisibility(View.GONE);
                            progressTxt.setVisibility(View.GONE);
                            buttonStatus(true);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                } );
        reqProgress.setVisibility(View.VISIBLE);
        reqProgress.bringToFront();
        progressTxt.setText("Raising Alarm ...");
        progressTxt.setVisibility(View.VISIBLE);
        requestQueue.add(jsonReq);
    };

    private View.OnClickListener triggerAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button tempBt = (Button)findViewById(v.getId());
//            System.out.println(tempBt.getText().toString());
            buttonStatus(false);

            Location triggerLocation = new Location(); // TODO Location API call {Async Thread}
            Location alarmLocation = triggerLocation.fetchLocation();
            String strAlarmLocation = "Lat: " + String.valueOf(alarmLocation.latitude) + " Long: " + String.valueOf(alarmLocation.longitude);
            String userID = setID;
            String alarmType = tempBt.getText().toString();
            Alarm alarm = new Alarm(userID, alarmType, alarmLocation);
            // TODO Send alarm to Server Endpoint {NEED async task extension of Alarm class}
            sendAlarmToServer(alarm);

        }
    };

}
