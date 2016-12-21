package com.ournet.weather.data;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class Place implements ILocation {
    public int id;
    public String name;
    public float longitude;
    public float latitude;
    public String country_code;
    public Place region;
    public boolean isSelected = false;

    private Map<String, String> names = new HashMap();

    public String name(String lang) {
        return names.containsKey(lang) ? names.get(lang) : this.name;
    }

    public static Place create(JSONObject data) {
        if (data == null) {
            return null;
        }
        Place place = new Place();
        place.names = new HashMap();

        try {
            place.id = data.getInt("id");
            place.name = data.getString("name");
            place.longitude = ((float) data.getDouble("longitude"));
            place.latitude = ((float) data.getDouble("latitude"));
            place.country_code = data.getString("country_code").toUpperCase();
            if (data.has("isSelected")) {
                place.isSelected = data.getBoolean("isSelected");
            }

            place.region = Place.create(data.getJSONObject("region"));


            // is from API
            if (data.has("alternatenames")) {
                String alternatenames = data.getString("alternatenames");
                if (alternatenames != null) {
                    String[] names = alternatenames.split(";");
                    for (int i = 0; i < names.length; i++) {
                        String name = names[i].substring(0, names[i].length() - 4);
                        String lang = names[i].substring(name.length() + 1, name.length() + 3);
                        place.names.put(lang, name);
                    }
                }
            }
            // is from json file
            else if (data.has("names")) {
                JSONObject names = data.getJSONObject("names");
//                if (names != null) {
//                    String[] names = alternatenames.split(";");
//                    for (int i = 0; i < names.length; i++) {
//                        String name = names[i].substring(0, names[i].length() - 4);
//                        String lang = names[i].substring(name.length() + 1, name.length() + 3);
//                        place.names.put(lang, name);
//                    }
//                }
            }
        } catch (Exception e) {

        }

        return place;
    }

    @Override
    public float getLongitude() {
        return longitude;
    }

    @Override
    public float getLatitude() {
        return latitude;
    }
}
