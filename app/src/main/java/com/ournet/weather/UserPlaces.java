package com.ournet.weather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ournet.weather.data.FileStorage;
import com.ournet.weather.data.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class UserPlaces {
    private List<Place> placesList = null;
    private Context context;

    public UserPlaces(Context context) {
        this.context = context;
    }

    public List<Place> get() {
        if (placesList == null) {
            placesList = load();
        }

        return placesList;
    }

    public Place get(int id) {
        for (Place place : get()) {
            if (place.id == id) {
                return place;
            }
        }
        return null;
    }

    public boolean add(Place place) {
        List<Place> places = get();

        for (Place p : places) {
            if (p.id == place.id) {
                return false;
            }
        }
        places.add(place);

        return save();
    }

    public boolean removeById(int id) {
        List<Place> places = get();

        for (Place p : places) {
            if (p.id == id) {
                return remove(p);
            }
        }
        return false;
    }

    public boolean remove(Place place) {
        List<Place> places = get();

        if (places.remove(place)) {
            return save();
        }
        return false;
    }

    public boolean setSelected(Place place) {

        add(place);

        List<Place> places = get();

        for (Place p : places) {
            p.isSelected = false;
        }

        place.isSelected = true;

        return true;
    }

    public Place getSelected() {
        List<Place> places = get();
        for (Place place : places) {
            if (place.isSelected) {
                return place;
            }
        }
        if (!places.isEmpty()) {
            Place place = places.get(0);
            place.isSelected = true;
            return place;
        }

        return null;
    }

    static ArrayList<Place> create(JSONObject json) throws JSONException {
        ArrayList<Place> places = new ArrayList();
        if (json == null || !json.has("places")) {
            return places;
        }
        JSONArray array = json.getJSONArray("places");
        for (int i = 0; i < array.length(); i++) {
            places.add(Place.create(array.getJSONObject(i)));
        }
        return places;
    }

    boolean save() {

        JSONObject json = new JSONObject();
        JSONArray places = new JSONArray();
        try {
            json.put("places", places);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        for (Place place : this.placesList) {
            places.put(place.getJson());
        }
        try {
            Log.i("place", "Saving places from file");
            return FileStorage.save(context, fileName(), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Place> load() {
        JSONObject json;
        try {
            json = FileStorage.loadJson(context, fileName());
            Log.i("place", "Loaded places from file");
            return create(json);
        } catch (IOException e) {
            return new ArrayList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    private static String fileName() {
        return "places";
    }
}
