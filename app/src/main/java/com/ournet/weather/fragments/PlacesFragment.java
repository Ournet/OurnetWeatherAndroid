package com.ournet.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ournet.weather.MainActivity;
import com.ournet.weather.R;
import com.ournet.weather.Settings;
import com.ournet.weather.UserPlaces;
import com.ournet.weather.Utils;
import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.data.Place;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user on 12/23/16.
 */

public class PlacesFragment extends BaseFragment {
    PlacesAdapter mPlacesAdapter;
    UserPlaces mUserPlaces;

    public void setPlaces(UserPlaces userPlaces) {
        this.mUserPlaces = userPlaces;
    }

    @Override
    public void placeChanged(Place place) {
        super.placeChanged(place);
        updateListView();
    }

    void updateListView() {
        if (this.mPlacesAdapter != null) {
            this.mPlacesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.places_listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = mUserPlaces.get(Integer.valueOf(id + ""));
                if (place != null) {
                    mUserPlaces.setSelected(place);

                    MainActivity activity = ((MainActivity) getActivity());
                    activity.goToForecast();
                    activity.placeChanged(place);
                }
            }
        });

        this.mPlacesAdapter = new PlacesAdapter(this.getContext());

        listView.setAdapter(this.mPlacesAdapter);

        return rootView;
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

            return vi;
        }

    }
}
