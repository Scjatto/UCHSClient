package com.example.uchs;


import android.text.format.DateFormat;

import java.util.Random;

public class Alarm {
    public String userID;
    public String alarmID;
    public String alarmType;
    public Location alarmLocation;
    public String alarmTimeStamp;

    private static final String ALLOWED_CHARACTERS ="qwertyuiopasdfghjklzxcvbnm";

//    private static String getRandomString(final int sizeOfRandomString)
//    {
//        final Random random=new Random();
//        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
//        for(int i=0;i<sizeOfRandomString;++i)
//            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
//        return sb.toString();
//    }

    Alarm(String user, String type, Location userLocation) {
        this.userID = user;
        this.alarmType = type;
        this.alarmLocation = userLocation;
        Long timeStamp = System.currentTimeMillis()/1000;
        this.alarmTimeStamp = String.valueOf(timeStamp);
//        this.alarmID = String.valueOf(timeStamp) + getRandomString(10);
        }

     public String humanReadableTimeStamp(String timeStamp) {
        long epochTime = Long.parseLong(timeStamp) * 1000;
        String dateTime = DateFormat.format("dd/MM/yyyy HH:mm:ss" , epochTime).toString();
        return dateTime;
     }
    }


