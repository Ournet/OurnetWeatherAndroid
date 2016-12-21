package com.ournet.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class UserLocation implements ILocation {
    public Float longitude;
    public Float latitude;
    public String country_code;

    public UserLocation(String country_code, float latitude, float longitude) {
        this.country_code = country_code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static UserLocation get() throws JSONException, IOException {
        JSONObject data = JsonClient.get("http://ip-api.com/json");
        String countryCode = data.getString("countryCode");
        float lat = (float) data.getDouble("lat");
        float lon = (float) data.getDouble("lon");

        return new UserLocation(countryCode, lat, lon);
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
