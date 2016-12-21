package com.ournet.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dumitru Cantea on 12/21/16.
 */

public class OurnetApi {

    private static JSONObject graphql(String json) throws IOException, JSONException {
        HashMap<String, String> props = new HashMap<>();
        props.put("Content-Type", "text/json");

        return JsonClient.post("http://10.0.2.2:41522/graphql", props, json.getBytes("UTF-8")).getJSONObject("data");
    }

    public static ForecastReport getForecast(ILocation location, ForecastDetails details) throws JSONException, IOException {

        String json = "{weatherReport(latitude:@latitude,longitude:@longitude)}";
        json = json.replace("@latitude", String.valueOf(location.getLatitude()));
        json = json.replace("@longitude", String.valueOf(location.getLongitude()));

        JSONObject data = graphql(json);

        return ForecastReport.create(data);
    }

    public static Place findPlace(int id) throws JSONException, IOException {
        String json = "{geoPlace(id=@id){id,name,alternatenames,country_code,region{id,name,alternatenames}}}";
        json = json.replace("@id", String.valueOf(id));

        JSONObject data = graphql(json);

        return Place.create(data);
    }

    public static Place findPlace(ILocation location) throws JSONException, IOException {
        String url = "http://api.geonames.org/findNearbyPlaceNameJSON?lat=@lat&lng=@lng&username=ournet&feature_class=P&fcode=PPL&fcode=PPLC&fcode=PPLA";
        url = url.replace("@lat", Float.toString(location.getLatitude()));
        url = url.replace("@lng", Float.toString(location.getLongitude()));

        JSONObject data = JsonClient.get(url);

        data = (JSONObject) data.getJSONArray("geonames").get(0);

        Place place = new Place();
        place.id = data.getInt("geonameId");
        place.name = data.getString("name");
        place.country_code = data.getString("countryCode");
        place.latitude = (float) data.getDouble("lat");
        place.longitude = (float) data.getDouble("lng");

        try {
            Place ournetPlace = OurnetApi.findPlace(place.id);
            if (ournetPlace != null) {
                place = ournetPlace;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return place;
    }

    public static class ForecastDetails {
        public Integer days;
        public ArrayList<String> dates;
    }
}
