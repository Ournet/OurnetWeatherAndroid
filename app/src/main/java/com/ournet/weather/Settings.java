package com.ournet.weather;

import com.ournet.weather.data.Place;
import com.ournet.weather.data.UserLocation;
import com.ournet.weather.data.OurnetApi;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class Settings {
    /**
     * Find user place
     */
    public final static Place exploreSelectedPlace() throws JSONException, IOException {
        UserLocation userLocation = null;
//        if (userLocation == null) {
        userLocation = getUserLocation();
//        }
        Place place = OurnetApi.findPlace(userLocation);

        return place;
    }

    public final static String language() {
        return Locale.getDefault().getLanguage();
    }

    public final static String country() {
        return Locale.getDefault().getCountry();
    }

    public static UserLocation getUserLocation() throws JSONException, IOException {
        return UserLocation.get();
    }
}