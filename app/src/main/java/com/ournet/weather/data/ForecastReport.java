package com.ournet.weather.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dumitru Cantea on 12/21/16.
 */

public class ForecastReport {
    public ArrayList<DayReport> days = new ArrayList<>();
    public String timezone;
    public Date updatedAt;
    private JSONObject json = null;
    public boolean fromCache;

    public static ForecastReport create(JSONObject json) throws JSONException {
        ForecastReport report = new ForecastReport();
        report.json = json;

        if (json.has("updatedAt")) {
            report.fromCache = true;
            Log.i("data", "has updatedAt");
            report.updatedAt = new Date(json.getLong("updatedAt"));
        } else {
            Log.i("data", "NO updatedAt");
            report.updatedAt = new Date();
            json.put("updatedAt", report.updatedAt.getTime());
            report.fromCache = false;
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

        public static DayReport create(JSONObject json) throws JSONException {
            DayReport report = new DayReport();

            report.stringDate = json.getString("date");
            report.date = java.sql.Date.valueOf(report.stringDate);

            JSONArray times = json.getJSONArray("times");
            for (int i = 0; i < times.length(); i++) {
                report.times.add(TimeReport.create((JSONObject) times.get(i)));
            }

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

        public static TimeReport create(JSONObject json) throws JSONException {
            TimeReport report = new TimeReport();

            report.date = new Date(json.getLong("time"));
            report.symbol = json.getJSONObject("symbol").getInt("number");
            report.windDirection = json.getJSONObject("wind").getJSONObject("dir").getString("code");
            report.windSpeed = (float) json.getJSONObject("wind").getJSONObject("speed").getDouble("mps");
            report.temperature = (float) json.getJSONObject("t").getDouble("value");
            report.pressure = (float) json.getJSONObject("pressure").getDouble("value");
            if (json.has("humidity")) {
                if (json.getJSONObject("humidity").has("percent")) {
                    report.humidity = (float) json.getJSONObject("humidity").getDouble("percent");
                }
            }
            if (json.has("cloudiness")) {
                if (json.getJSONObject("cloudiness").has("percent")) {
                    report.cloudiness = (float) json.getJSONObject("cloudiness").getDouble("percent");
                }
            }
            if (json.has("fog")) {
                if (json.getJSONObject("fog").has("percent")) {
                    report.fog = (float) json.getJSONObject("fog").getDouble("percent");
                }
            }

            return report;
        }
    }

    public boolean save(Context context, ILocation location) {
        try {
            return TempFileStorage.save(context, fileName(location), this.json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ForecastReport load(Context context, ILocation location) {
        try {
            JSONObject json = TempFileStorage.loadJson(context, fileName(location));
            return ForecastReport.create(json);
        } catch (IOException e) {
//            e.printStackTrace();
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
