package com.example.uchs;

import android.util.Log;

import java.util.Random;

public class Location {

    public double latitude;
    public double longitude;
    public String locName;

    private Location[] initLocationPool() {

        Location[] locationPool = new Location[10];

        locationPool[0] = new Location();
        locationPool[0].latitude = 22.568237;
        locationPool[0].longitude = 88.411652;
        locationPool[0].locName = "LocationAlpha";

        locationPool[1] = new Location();
        locationPool[1].latitude = 22.569614;
        locationPool[1].longitude = 88.413124;
        locationPool[1].locName = "LocationBeta";

        locationPool[2] = new Location();
        locationPool[2].latitude = 22.592463;
        locationPool[2].longitude = 88.404259;
        locationPool[2].locName = "LocationGamma";

        locationPool[3] = new Location();
        locationPool[3].latitude = 22.577522;
        locationPool[3].longitude = 88.431343;
        locationPool[3].locName = "LocationDelta";

        locationPool[4] = new Location();
        locationPool[4].latitude = 22.589142;
        locationPool[4].longitude = 88.410873;
        locationPool[4].locName = "LocationEpsilon";

        locationPool[5] = new Location();
        locationPool[5].latitude = 22.592569;
        locationPool[5].longitude = 88.415283;
        locationPool[5].locName = "LocationZeta";

        locationPool[6] = new Location();
        locationPool[6].latitude = 22.570270;
        locationPool[6].longitude = 88.429416;
        locationPool[6].locName = "LocationEta";

        locationPool[7] = new Location();
        locationPool[7].latitude = 22.568074;
        locationPool[7].longitude = 88.432449;
        locationPool[7].locName = "LocationTheta";

        locationPool[8] = new Location();
        locationPool[8].latitude = 22.570268;
        locationPool[8].longitude = 88.429509;
        locationPool[8].locName = "LocationIota";

        locationPool[9] = new Location();
        locationPool[9].latitude = 22.574622;
        locationPool[9].longitude = 88.433904;
        locationPool[9].locName = "LocationKappa";

        return locationPool;
    }

    public Location fetchLocation() {
        // TODO Call location API in an ASYNC Thread
        Location triggerLocation = new Location();
        Location[] pool = triggerLocation.initLocationPool();
        Random random = new Random();
        int randIdx = random.nextInt(10);
        Log.d("LOCATION", "Selected Location: " + pool[randIdx].locName);

//        triggerLocation.latitude = 22.57459442393395;
//        triggerLocation.longitude = 88.43391573460059;

//        return triggerLocation;
        return pool[randIdx];
    }

    public String getLocationName(String location) {

        String locationName = "Locatable Location";
        return locationName;
    }
}
