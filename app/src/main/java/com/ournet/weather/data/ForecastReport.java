package com.ournet.weather.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dumitru Cantea on 12/21/16.
 */

public class ForecastReport {
    public ArrayList<DayReport> days = new ArrayList<>();
    public String timezone;
    public Date updatedAt;
    private JSONObject json = null;

    public static ForecastReport create(JSONObject json) throws JSONException {
        ForecastReport report = new ForecastReport();
        report.json = json;

        if (json.has("updatedAt")) {
            report.updatedAt = new Date(json.getLong("updatedAt"));
        } else {
            report.updatedAt = new Date();
            json.put("updatedAt", report.updatedAt.getTime());
        }

        if (json.has("timezone")) {
            report.timezone = json.getString("timezone");
        }

        if (!json.has("days")) {
            throw new JSONException("Weather json must contains .days");
        }
        JSONArray days = json.getJSONArray("days");
        for (int i = 0; i < days.length(); i++) {
            report.days.add(DayReport.create((JSONObject) days.get(i)));
        }

        return report;
    }

    public static class DayReport {
        public String stringDate;
        public Date date;
        public ArrayList<TimeReport> times = new ArrayList<>();

        public static DayReport create(JSONObject json) {
            DayReport report = new DayReport();

            return report;
        }
    }

    public static class TimeReport {
        public Date date;
        public Integer symbol;
        public String symbolName;
        public String windDirection;
        public Float windSpeed;
        public Float temperature;
        public Float pressure;
        public Float humidity;
        public Float cloudiness;
        public Float fog;

        public static TimeReport create(JSONObject json) {
            TimeReport report = new TimeReport();

            return report;
        }
    }

    public boolean save(Context context, ILocation location) {

        try {
            return FileStorage.save(context, fileName(location), this.json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ForecastReport load(Context context, ILocation location) {
        JSONObject json;
        try {
            json = FileStorage.loadJson(context, fileName(location));
            return ForecastReport.create(json);
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String fileName(ILocation location) {
        DecimalFormat format = new DecimalFormat("#.##");
        return "wr-" + format.format(location.getLatitude()) + ':' + format.format(location.getLongitude());
    }
}
