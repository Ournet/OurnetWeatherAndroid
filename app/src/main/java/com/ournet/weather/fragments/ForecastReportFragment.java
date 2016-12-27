package com.ournet.weather.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import com.ournet.weather.MainActivity;
import com.ournet.weather.R;
import com.ournet.weather.Utils;
import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.fragments.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by user on 12/22/16.
 */

public class ForecastReportFragment extends BaseFragment {
    ForecastReport report;
    ForecastDaysAdapter forecastAdapter;
    ListView listView = null;

    public void setForecastReport(ForecastReport report) {
        Log.i("adapter", "set report");
        this.report = report;

        if (report != null) {
//            String timezone = report.timezone == null ? mPlace.timezone : report.timezone;
            Calendar calendar = new GregorianCalendar();
//            if (timezone != null) {
//                calendar.setTimeZone(TimeZone.getTimeZone(timezone));
//            } else {
//                Log.w("data", "Timezone is null");
//            }
            ForecastReport.DayReport firstDay = report.days.get(0);
            long currentTime = calendar.getTimeInMillis();
            Log.i("data", "currentDate=" + calendar.getTime());
            ArrayList<ForecastReport.TimeReport> timesToRemove = new ArrayList();

            for (ForecastReport.TimeReport time : firstDay.times) {
                if (time.date.getTime() < currentTime) {
                    timesToRemove.add(time);
                }
            }
            for (ForecastReport.TimeReport time : timesToRemove) {
                firstDay.times.remove(time);
            }
            if (firstDay.times.size() == 0) {
                report.days.remove(firstDay);
            }
        }

        if (forecastAdapter != null) {
            forecastAdapter.notifyDataSetChanged();
        }

        if (listView != null) {
//            listView.setSelection(0);
//            listView.setSelectionAfterHeaderView();

//            Log.i("view", "setViewData position 0");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecastreport, container, false);

        listView = (ListView) rootView.findViewById(R.id.fragment_forecastreport_list);
        View footerView = inflater.inflate(R.layout.forecast_footer, container, false);
        TextView textView = (TextView) footerView.findViewById(R.id.forecast_footer_text);

        ForecastDaysAdapter forecastAdapter = new ForecastDaysAdapter(this.getContext());
        this.forecastAdapter = forecastAdapter;

        listView.setAdapter(forecastAdapter);
//        listView.setSelection(0);
//        Log.i("setSelection", "aici");

        listView.addFooterView(footerView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onClickMoreForecast(v);
            }
        });
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
            return l > 6 ? 6 : l;
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
            LinearLayout containerView = (LinearLayout) vi.findViewById(R.id.item_weather_report_day_container);

            ForecastReport.DayReport data = report.days.get(position);

            TextView dateView = (TextView) vi.findViewById(R.id.item_weather_report_day_date);
            SimpleDateFormat dformat = new SimpleDateFormat("E, d MMM", Locale.getDefault());
            dateView.setText(dformat.format(data.date));

            for (int i = 0; i < data.times.size(); i++) {
                View row = inflater.inflate(R.layout.item_weather_report_time, null);
                ForecastReport.TimeReport time = data.times.get(i);
                TextView textView = (TextView) row.findViewById(R.id.item_weather_report_time_date);
                textView.setText(time.date.toString());
                dformat = new SimpleDateFormat("HH:00", Locale.getDefault());
                textView.setText(dformat.format(time.date));
                textView = (TextView) row.findViewById(R.id.item_weather_report_time_temperature);
                textView.setText(String.valueOf(time.temperature.intValue()) + "°");
                textView = (TextView) row.findViewById(R.id.item_weather_report_time_wind_speed);
                textView.setText(time.windSpeed.toString());
                textView = (TextView) row.findViewById(R.id.item_weather_report_time_pressure);
                textView.setText(time.pressure.toString());
                textView = (TextView) row.findViewById(R.id.item_weather_report_time_info);
                textView.setText(Utils.weatherSymbolName(getResources(), time.symbol));


                ImageView imageView = (ImageView) row.findViewById(R.id.item_weather_report_time_image);
                imageView.setImageDrawable(getResources().getDrawable(Utils.weatherIcon(time.symbol)));



//                if (i == data.times.size() - 1) {
//                    row.findViewById(R.id.item_weather_report_time_separator).setVisibility(View.INVISIBLE);
//                }

                containerView.addView(row);
            }

            return vi;
        }

    }
}
