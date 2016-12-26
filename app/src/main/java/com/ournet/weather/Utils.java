package com.ournet.weather;

import com.ournet.weather.data.Place;

/**
 * Created by user on 12/23/16.
 */

public class Utils {
    public static String regionName(Place place) {
        String name = place.country_code==null ? "": place.country_code.toUpperCase();
        if (place.region != null) {
            if(name.length()>0) {
                name = Utils.name(place.region) + ", " + name;
            }else{
                name = Utils.name(place.region);
            }
        }
        return name;
    }

    public static String name(Place place) {
        return place.name(Settings.language());
    }
}
