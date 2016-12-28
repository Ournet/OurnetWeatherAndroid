package com.ournet.weather.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ournet.weather.MainActivity;
import com.ournet.weather.OnLoadingTask;
import com.ournet.weather.OnPlaceChanged;
import com.ournet.weather.R;
import com.ournet.weather.data.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru Cantea on 12/22/16.
 */

public class BaseFragment extends Fragment implements OnPlaceChanged, OnLoadingTask {
    protected Place mPlace;
    List<OnLoadingTask> onLoadingTasks = new ArrayList();

    @Override
    public void onPlaceChanged(Place place) {
        mPlace = place;
    }

    public void addOnLoadingTasks(OnLoadingTask onLoadingTask) {
        onLoadingTasks.add(onLoadingTask);
    }

    @Override
    public void onStartLoadingTask() {
        for (OnLoadingTask onLoadingTask : onLoadingTasks) {
            onLoadingTask.onStartLoadingTask();
        }
    }

    @Override
    public void onEndLoadingTask() {
        for (OnLoadingTask onLoadingTask : onLoadingTasks) {
            onLoadingTask.onEndLoadingTask();
        }
    }

    protected void logEvent(String name, Bundle bundle) {
        ((MainActivity) getActivity()).logEvent(name, bundle);
    }

    protected void snackbar(int resource) {
        ((MainActivity) getActivity()).snackbar(resource);
    }
}
