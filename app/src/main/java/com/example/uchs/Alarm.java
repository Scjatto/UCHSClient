package com.example.uchs;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.AbstractQueue;

public class Alarm {
    public String userID;
    private String alarmType;
    private Location alarmLocation;

    Alarm(String user, String type, Location userLocation) {
        this.userID = user;
        this.alarmType = type;
        this.alarmLocation = userLocation;
    }

}
