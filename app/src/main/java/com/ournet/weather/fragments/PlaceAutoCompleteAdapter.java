package com.ournet.weather.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ournet.weather.R;
import com.ournet.weather.Utils;
import com.ournet.weather.data.OurnetApi;
import com.ournet.weather.data.Place;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/26/16.
 */

public class PlaceAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<Place> resultList = new ArrayList<Place>();

    public PlaceAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Place getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(Utils.name(getItem(position)));
        ((TextView) convertView.findViewById(R.id.text2)).setText(Utils.regionName(getItem(position)));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Place> places = findPlaces(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = places;
                    filterResults.count = places.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Place>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<Place> findPlaces(Context context, String query) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        try {
            return OurnetApi.findPlaces(query, null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
