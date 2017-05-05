package com.ournet.weather.data;

import android.os.AsyncTask;

import com.ournet.weather.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dumitru Cantea on 12/21/16.
 */

public class OurnetApi {

    private static JSONObject graphql(String json) throws IOException, JSONException {
        HashMap<String, String> props = new HashMap<>();
        props.put("Content-Type", "application/json");
        props.put("Accept", "application/json");

        return JsonClient.post("http://ournetapi.com/graphql", props, json).getJSONObject("data");
    }

    public static ForecastReport getForecast(ILocation location, ForecastDetails details) throws JSONException, IOException {

        String json = "{\"query\": \"{report:weather_report(latitude:@latitude,longitude:@longitude,days:@days)}\"}";
        json = json.replace("@latitude", String.valueOf(location.getLatitude()));
        json = json.replace("@longitude", String.valueOf(location.getLongitude()));
        String days = "null";
        if (details != null && details.days > 0) {
            days = details.days.toString();
        }

        json = json.replace("@days", days);

        JSONObject data = graphql(json).getJSONObject("report");

        return ForecastReport.create(data);
    }

    public static Place findPlace(int id) throws JSONException, IOException {
        String json = "{\"query\":\"{place:places_place(id:@id){id,name,alternatenames,timezone,country_code,longitude,latitude,region{id,name,alternatenames}}}\"}";
        json = json.replace("@id", String.valueOf(id));

        JSONObject data = graphql(json).getJSONObject("place");

        return Place.create(data);
    }

    public static Place taskFindPlace(int id) {
        try {
            return new PlaceTask().execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Place> findPlaces(String query, String country) throws JSONException, IOException {
        String json = "{\"query\":\"{places:places_searchPlace(query:\\\"@query\\\",country:@country,limit:5){id,name,alternatenames,country_code,region{name,alternatenames}}}\"}";
        json = json.replace("@query", query.replaceAll("\"",""));
        if(country==null || country.length()!=2){
            json = json.replace("@country", "null");
        }else{
            json = json.replace("@country", "\\\""+country+"\\\"");
        }

        JSONArray data = graphql(json).getJSONArray("places");

        return Place.create(data);
    }

    public static Place findPlace(ILocation location) throws JSONException, IOException {
        String url = "http://api.geonames.org/findNearbyPlaceNameJSON?lat=@lat&lng=@lng&username=ournet&feature_class=P&fcode=PPL&fcode=PPLC&fcode=PPLA";
        url = url.replace("@lat", Double.toString(location.getLatitude()));
        url = url.replace("@lng", Double.toString(location.getLongitude()));

        JSONObject data = JsonClient.get(url);

        data = (JSONObject) data.getJSONArray("geonames").get(0);

        Place place = new Place();
        place.id = data.getInt("geonameId");
        place.name = data.getString("name");
        place.country_code = data.getString("countryCode");
        place.latitude = data.getDouble("lat");
        place.longitude = data.getDouble("lng");

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

    static class PlaceTask extends AsyncTask<Integer, Void, Place> {

        @Override
        protected Place doInBackground(Integer... params) {
            try {
                return OurnetApi.findPlace(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
