package com.example.uchs;

public class Location {

    public double latitude;
    public double longitude;

    public Location fetchLocation() {
        // TODO Call location API in an ASYNC Thread
        Location triggerLocation = new Location();
        triggerLocation.latitude = 22.57459442393395;
        triggerLocation.longitude = 88.43391573460059;

        return triggerLocation;
    }

    public String getLocationName(String location) {

        String locationName = "Locatable Location";
        return locationName;
    }
}
