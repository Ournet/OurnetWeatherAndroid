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
            case 20:
            case 23:
            case 24:
            case 26:
            case 30:
            case 31:
            case 40:
            case 42:
            case 46:
            case 47:
                return R.mipmap.wi_7;
            case 8:
            case 21:
            case 28:
            case 33:
            case 44:
            case 49:
                return R.mipmap.wi_8;
            case 9:
            case 22:
                return R.mipmap.wi_9;
            case 10:
            case 25:
            case 41:
                return R.mipmap.wi_10;
            case 11:
                return R.mipmap.wi_11;
            case 12:
            case 27:
            case 32:
            case 43:
            case 48:
                return R.mipmap.wi_12;
            case 13:
            case 29:
            case 45:
            case 50:
                return R.mipmap.wi_13;
            case 14:
            case 34:
                return R.mipmap.wi_14;
            case 15:
                return R.mipmap.wi_15;
            case 16:
                return R.mipmap.wi_14;
        }
        return R.mipmap.wi_1;
    }

    public static String weatherSymbolName(Resources resources, int symbol) {
        String[] names = resources.getStringArray(R.array.weather_symbol_names);
        if (symbol > names.length - 1) {
            symbol = 1;
        }
        return names[symbol - 1];
    }
}
