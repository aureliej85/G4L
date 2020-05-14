package fr.aureliejosephine.go4lunch.ui;

import android.location.Location;

import java.util.List;

import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;

public abstract class Utils {

    /*public static void updateDistanceToCurrentLocation(Location currentLocation, List<DetailsResult> restaurantList)
    {
        Location restaurantLocation = new Location("fusedLocationProvider");
        int size = restaurantList.size();
        for (int i = 0; i < size; i++)
        {
            //Get the restaurant's location
            restaurantLocation.setLatitude(restaurantList.get(i).getGeometry().getLocation().getLat());
            restaurantLocation.setLongitude(restaurantList.get(i).getGeometry().getLocation().getLng());
            //Get the distance between currentLocation and restaurantLocation
            int distanceLocation = (int) currentLocation.distanceTo(restaurantLocation);
            restaurantList.get(i).setDistance(distanceLocation);
        }
    }*/
}
