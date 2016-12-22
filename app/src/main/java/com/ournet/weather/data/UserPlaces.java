package com.ournet.weather.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class UserPlaces {
    private List<Place> placesList = null;

    public List<Place> get() {
        if (placesList == null) {
            placesList = load();
        }

        return placesList;
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

    boolean save() {
        return false;
    }

    List<Place> load() {
        return new ArrayList<Place>();
    }
}
