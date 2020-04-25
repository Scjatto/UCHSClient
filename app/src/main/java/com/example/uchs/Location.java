package com.example.uchs;

public class Location {

    public double latitude;
    public double longitude;

    public Location fetchLocation() {
        // TODO Call location API in an ASYNC Thread
        Location triggerLocation = new Location();
        triggerLocation.latitude = 45.32;
        triggerLocation.longitude = 160.42;

        return triggerLocation;
    }
}
