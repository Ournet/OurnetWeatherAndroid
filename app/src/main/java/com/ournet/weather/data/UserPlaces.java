package com.ournet.weather.data;

import java.util.ArrayList;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class UserPlaces {
    private static ArrayList<Place> places;

    public static ArrayList<Place> get() {
        if (places == null) {
            places = new ArrayList();
        }

        return places;
    }

    public static boolean add(Place place) {
        ArrayList<Place> places = UserPlaces.get();

        for (Place p : places) {
            if (p.id == place.id) {
                return false;
            }
        }
        return places.add(place);
    }

    public static boolean remove(int id) {
        ArrayList<Place> places = UserPlaces.get();

        for (Place p : places) {
            if (p.id == id) {
                return places.remove(p);
            }
        }
        return false;
    }
}
