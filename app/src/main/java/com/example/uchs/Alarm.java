package com.example.uchs;


import java.util.Random;

public class Alarm {
    public String userID;
    public String alarmID;
    public String alarmType;
    public Location alarmLocation;
    public String alarmTimeStamp;

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    Alarm(String user, String type, Location userLocation) {
        this.userID = user;
        this.alarmID = getRandomString(10);
        this.alarmType = type;
        this.alarmLocation = userLocation;
//        Date currentTime = Calendar.getInstance().getTime();
//        this.alarmTimeStamp = currentTime.toString().replace(" ","");
        Long timeStamp = System.currentTimeMillis()/1000;
        this.alarmTimeStamp = String.valueOf(timeStamp);
        }

    }


