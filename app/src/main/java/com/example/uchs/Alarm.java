package com.example.uchs;

public class Alarm {
    public String userID;
    public String alarmType;
    public Location alarmLocation;

    Alarm(String user, String type, Location userLocation) {
        this.userID = user;
        this.alarmType = type;
        this.alarmLocation = userLocation;
    }


}
