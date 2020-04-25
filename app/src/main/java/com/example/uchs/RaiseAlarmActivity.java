package com.example.uchs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RaiseAlarmActivity extends AppCompatActivity {

    private Button raise;
    private TextView raiseLabel;
    private Button medical;
    private Button fire;
    private Button lost;
    private String setID;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_alarm);

        setID = getIntent().getStringExtra("RAISE_ALARM_ID");
        getSupportActionBar().setTitle(setID);

        requestQueue = Volley.newRequestQueue(this);

        raise = (Button)findViewById(R.id.btRaiseAlarm);
        raiseLabel = (TextView)findViewById(R.id.txtRaiseAlarm);
        medical = (Button)findViewById(R.id.btMedical);
        fire = (Button)findViewById(R.id.btFire);
        lost = (Button)findViewById(R.id.btLost);

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

    public void sendAlarmToServer(Alarm alarm) {
        String url = String.format("https://tribal-marker-274610.el.r.appspot.com/testname?name=%1$s", alarm.userID);
        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject res = response.getJSONObject("data");
                            System.out.println(res);
                        } catch (JSONException e) {
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
        requestQueue.add(jsonreq);
    };

    private View.OnClickListener triggerAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button tempBt = (Button)findViewById(v.getId());
            System.out.println(tempBt.getText().toString());

            Location triggerLocation = new Location(); // TODO Location API call {Async Thread}
            Location alarmLocation = triggerLocation.fetchLocation();
            String strAlarmLocation = "Lat: " + String.valueOf(alarmLocation.latitude) + " Long: " + String.valueOf(alarmLocation.longitude);
            String userID = setID;
            String alarmType = tempBt.getText().toString();
            Alarm alarm = new Alarm(userID, alarmType, alarmLocation);
            // TODO Send alarm to Server Endpoint {NEED async task extension of Alarm class}
            sendAlarmToServer(alarm);
            Intent alarmIntent = new Intent(RaiseAlarmActivity.this,AlarmStatusActivity.class);
            Bundle alarmBundle = new Bundle();
            alarmBundle.putString("USER_ID",userID);
            alarmBundle.putString("ALARM_TYPE",alarmType);
            alarmBundle.putString("ALARM_LOCATION",strAlarmLocation);
            alarmIntent.putExtras(alarmBundle);
            startActivity(alarmIntent);

        }
    };

}
