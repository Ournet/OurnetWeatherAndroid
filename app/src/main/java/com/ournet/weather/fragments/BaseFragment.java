package com.ournet.weather.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.ournet.weather.OnLoadingTask;
import com.ournet.weather.OnPlaceChanged;
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
}
