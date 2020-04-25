package com.example.uchs;

public class Location {

    public double latitude;
    public double longitude;

    public Location fetchLocation() {
        // TODO Call location API in an ASYNC Thread
        Location triggerLocation = new Location();
        triggerLocation.latitude = 22.516085;
        triggerLocation.longitude = 88.388869;

        return triggerLocation;
    }
}
