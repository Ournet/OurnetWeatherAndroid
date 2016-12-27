package com.ournet.weather;

import android.content.res.Resources;

import com.ournet.weather.data.Place;

/**
 * Created by user on 12/23/16.
 */

public class Utils {
    public static String regionName(Place place) {
        String name = place.country_code == null ? "" : place.country_code.toUpperCase();
        if (place.region != null) {
            if (name.length() > 0) {
                name = Utils.name(place.region) + ", " + name;
            } else {
                name = Utils.name(place.region);
            }
        }
        return name;
    }

    public static String name(Place place) {
        return place.name(Settings.language());
    }

    public static int weatherIcon(int symbol) {
        switch (symbol) {
            case 1:
                return R.mipmap.wi_1;
            case 2:
                return R.mipmap.wi_2;
            case 3:
                return R.mipmap.wi_3;
            case 4:
                return R.mipmap.wi_4;
            case 5:
                return R.mipmap.wi_5;
            case 6:
                return R.mipmap.wi_6;
            case 7:
                return R.mipmap.wi_7;
            case 8:
                return R.mipmap.wi_8;
            case 9:
                return R.mipmap.wi_9;
            case 10:
                return R.mipmap.wi_10;
            case 11:
                return R.mipmap.wi_11;
            case 12:
                return R.mipmap.wi_12;
            case 13:
                return R.mipmap.wi_13;
            case 14:
                return R.mipmap.wi_14;
            case 15:
                return R.mipmap.wi_15;
            case 16:
                return R.mipmap.wi_14;
        }
        return 0;
    }

    public static String weatherSymbolName(Resources resources, int symbol) {
        String[] names = resources.getStringArray(R.array.weather_symbol_names);
        return names[symbol + 1];
    }
}
