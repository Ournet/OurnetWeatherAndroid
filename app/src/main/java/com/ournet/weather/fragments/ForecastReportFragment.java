package com.ournet.weather.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ournet.weather.R;
import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.fragments.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by user on 12/22/16.
 */

public class ForecastReportFragment extends BaseFragment {
    ForecastReport report;
    ForecastDaysAdapter forecastAdapter;

    public void setForecastReport(ForecastReport report) {
        Log.i("adapter", "set report");
        this.report = report;
        if (forecastAdapter != null) {
            forecastAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecastreport, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.fragment_forecastreport_list);
        ForecastDaysAdapter forecastAdapter = new ForecastDaysAdapter(this.getContext());
        this.forecastAdapter = forecastAdapter;
        listView.setAdapter(forecastAdapter);

        return rootView;
    }

    class ForecastDaysAdapter extends BaseAdapter {

        Context context;

        private LayoutInflater inflater = null;

        public ForecastDaysAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (report == null) {
                return 0;
            }
            int l = report.days.size();
            return l;
//            return l > 6 ? 6 : l;
        }

        @Override
        public Object getItem(int position) {
            return report.days.get(position);
        }

        @Override
        public long getItemId(int position) {
            return report.days.get(position).date.getTime();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
//            if (vi == null) {
                vi = inflater.inflate(R.layout.item_weather_report_day, null);
//            }
            ListView listView = (ListView) vi.findViewById(R.id.item_weather_report_day_list);

            ForecastReport.DayReport data = report.days.get(position);

            TextView dateView = (TextView) vi.findViewById(R.id.item_weather_report_day_date);
            SimpleDateFormat dformat= new SimpleDateFormat("E, d MMM", Locale.getDefault());
            dateView.setText(dformat.format(data.date));

            listView.setAdapter(new ForecastTimesAdapter(this.context, report.days.get(position).times));

            return vi;
        }
    }

    class ForecastTimesAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;
        ArrayList<ForecastReport.TimeReport> times;

        public ForecastTimesAdapter(Context context, ArrayList<ForecastReport.TimeReport> times) {
            this.context = context;
            this.times = times;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return times.size();
        }

        @Override
        public Object getItem(int position) {
            return times.get(position);
        }

        @Override
        public long getItemId(int position) {
            return times.get(position).date.getTime();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (vi == null) {
                vi = inflater.inflate(R.layout.item_weather_report_time, null);
            }
            ForecastReport.TimeReport data = times.get(position);
            TextView textView = (TextView) vi.findViewById(R.id.item_weather_report_time_date);
            textView.setText(data.date.toString());
            SimpleDateFormat dformat= new SimpleDateFormat("HH:00", Locale.getDefault());
            textView.setText(dformat.format(data.date));
            textView = (TextView) vi.findViewById(R.id.item_weather_report_time_temperature);
            textView.setText(String.valueOf(data.temperature.intValue())+"°");
            textView = (TextView) vi.findViewById(R.id.item_weather_report_time_wind_speed);
            textView.setText(data.windSpeed.toString());
            textView = (TextView) vi.findViewById(R.id.item_weather_report_time_pressure);
            textView.setText(data.pressure.toString());

            Log.i("adapter", "time view" + position + " - " + data.date.toString());

            return vi;
        }
    }
}
