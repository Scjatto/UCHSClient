package com.example.uchs;

import android.text.format.DateFormat;

public class Alert {

    private String alertAlarmType;
    private double alertAlarmLat;
    private double alertAlarmLong;
    private long alertAlarmTS;
    private String alertAlarmUser;

    Alert(String alarmType, double alarmLat, double alarmLong, long alarmTS, String alarmUser){
        alertAlarmType = alarmType;
        alertAlarmLat = alarmLat;
        alertAlarmLong = alarmLong;
        alertAlarmTS = alarmTS;
        alertAlarmUser = alarmUser;
    }

    private String humanReadableTimeStamp(long epochTimeStamp){
        long epochTime = epochTimeStamp * 1000;
        String dateTime = DateFormat.format("dd/MM/yyyy HH:mm:ss" , epochTime).toString();
        return dateTime;
    }

    private String getLocationString(double lat, double lon) {
        Location location = new Location();
        String locName = location.getLocationName(lat, lon);
        if (locName != null) {
            return locName;
        }
        return "UnIdentified Location";
    }

    public String genAlertMsg(){
        String msg = "An alarm of type ";
        msg += alertAlarmType;
        msg += " was raised by " + alertAlarmUser;

        return msg;
    }

    public String genExpandedMsg() {
        String msg = "An alarm of type ";
        msg += alertAlarmType;
        msg += " was raised by " + alertAlarmUser;
        msg += " from " + getLocationString(alertAlarmLat, alertAlarmLong);
        msg += " on " + humanReadableTimeStamp(alertAlarmTS);

        return msg;
    }

    public String genDetailedMsg() {
        String msg = "Alarm Type: " + alertAlarmType + "\n";
        msg += "Raised By: " + alertAlarmUser + "\n";
        msg += "Location: " + getLocationString(alertAlarmLat, alertAlarmLong) + "\n";
        msg += "Time of Raised Alarm: " + humanReadableTimeStamp(alertAlarmTS) + "\n";

        return msg;
    }
}
