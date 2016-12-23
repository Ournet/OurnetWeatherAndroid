package com.ournet.weather.fragments;

import android.support.v4.app.Fragment;

import com.ournet.weather.OnPlaceChanged;
import com.ournet.weather.data.Place;

/**
 * Created by Dumitru Cantea on 12/22/16.
 */

public class BaseFragment extends Fragment implements OnPlaceChanged {
    protected Place mPlace;

    @Override
    public void placeChanged(Place place) {
        mPlace = place;
    }
}
