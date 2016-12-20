package com.ournet.weather.data;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class UserSettings {
    public static String language() {
        return Locale.getDefault().getLanguage();
    }

    public static UserLocation getUserLocation() throws JSONException, IOException {
        return UserLocation.get();
    }

    public static UserPlaces places;
}