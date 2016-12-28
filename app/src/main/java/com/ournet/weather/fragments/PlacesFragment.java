package com.ournet.weather.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ournet.weather.MainActivity;
import com.ournet.weather.OnPlaceChanged;
import com.ournet.weather.R;
import com.ournet.weather.Settings;
import com.ournet.weather.UserPlaces;
import com.ournet.weather.Utils;
import com.ournet.weather.data.OurnetApi;
import com.ournet.weather.data.Place;
import com.ournet.weather.ui.DelayAutoCompleteTextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dumitru Cantea on 12/23/16.
 */

public class PlacesFragment extends BaseFragment {
    PlacesAdapter mPlacesAdapter;
    UserPlaces mUserPlaces;
    List<OnPlaceChanged> onPlaceChangedList = new ArrayList();


    @Override
    public void onPlaceChanged(Place place) {
        super.onPlaceChanged(place);

        updateListView();

        for (OnPlaceChanged placeChanged : onPlaceChangedList) {
            placeChanged.onPlaceChanged(place);
        }
    }

    void updateListView() {
        if (this.mPlacesAdapter != null) {
            this.mPlacesAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);

        mUserPlaces = new UserPlaces(getContext());

        ListView listView = (ListView) rootView.findViewById(R.id.places_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = mUserPlaces.get(Integer.valueOf(id + ""));
                if (place != null) {
                    mUserPlaces.setSelected(place);

                    onPlaceChanged(place);
                }
            }
        });

        this.mPlacesAdapter = new PlacesAdapter(this.getContext());

        listView.setAdapter(this.mPlacesAdapter);

        final DelayAutoCompleteTextView placeTitle = (DelayAutoCompleteTextView) rootView.findViewById(R.id.place_search_title);
        placeTitle.setThreshold(3);
        placeTitle.setAdapter(new PlaceAutoCompleteAdapter(this.getContext()));
        placeTitle.setLoadingIndicator(
                (android.widget.ProgressBar) rootView.findViewById(R.id.place_search_loading_indicator));
        placeTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Place place = (Place) adapterView.getItemAtPosition(position);
                place = OurnetApi.taskFindPlace(place.id);
                if (place != null) {
                    placeTitle.setText(Utils.name(place));
                    mUserPlaces.setSelected(place);
                    onPlaceChanged(place);
                }
            }
        });

        explorePlace();

        return rootView;
    }

    public void addOnPlaceChanged(OnPlaceChanged placeChanged) {
        onPlaceChangedList.add(placeChanged);
    }

    private void explorePlace() {
        onStartLoadingTask();

//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            // fetch data
//        } else {
//            // display error
//        }
        Place place = mUserPlaces.getSelected();
        if (place == null) {
            try {
                place = new PlaceTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (place != null) {
                mUserPlaces.setSelected(place);
            }
        }

        onEndLoadingTask();

        onPlaceChanged(place);

    }

    class PlacesAdapter extends BaseAdapter {

        Context context;

        private LayoutInflater inflater = null;

        public PlacesAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (mUserPlaces == null) {
                return 0;
            }
            return mUserPlaces.get().size();
        }

        @Override
        public Object getItem(int position) {
            return mUserPlaces.get().get(position);
        }

        @Override
        public long getItemId(int position) {
            return mUserPlaces.get().get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
//            if (vi == null) {
            vi = inflater.inflate(R.layout.listitem_place, null);
//            }

            Place data = mUserPlaces.get().get(position);

            TextView textView = (TextView) vi.findViewById(R.id.listitem_place_name);
            textView.setText(Utils.name(data));

            textView = (TextView) vi.findViewById(R.id.listitem_place_region);
            textView.setText(Utils.regionName(data));

            View deleteView = vi.findViewById(R.id.place_delete_icon);
            deleteView.setTag(data);
            if (data.isSelected) {
                vi.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                deleteView.setVisibility(View.GONE);
            } else {
                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Place tag = (Place) v.getTag();
                        mUserPlaces.removeById(tag.id);
                        mPlacesAdapter.notifyDataSetChanged();
                    }
                });
            }

            return vi;
        }

    }

    class PlaceTask extends AsyncTask<String, Void, Place> {

        @Override
        protected Place doInBackground(String... params) {
            try {
                return Settings.exploreSelectedPlace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
