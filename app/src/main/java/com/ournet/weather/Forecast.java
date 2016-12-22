package com.ournet.weather;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.text.format.DateUtils;
import android.util.Log;

import com.ournet.weather.data.ForecastReport;
import com.ournet.weather.data.ILocation;
import com.ournet.weather.data.OurnetApi;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Dumitru Cantea on 12/22/16.
 */

public class Forecast {
    Context context;

    public Forecast(Context context) {
        this.context = context;
    }

    public ForecastReport getReport(ILocation location, Date minDate) throws IOException, JSONException {

        if (minDate == null) {
            minDate = defaultMinDate();
        }

        ForecastReport report;

        report = ForecastReport.load(this.context, location);
        if (report != null) {
            if (report.updatedAt.getTime() > minDate.getTime()) {
                return report;
            }
        }

        OurnetApi.ForecastDetails details = new OurnetApi.ForecastDetails();
        details.days = 6;
        report = OurnetApi.getForecast(location, details);

        if (report != null) {
            report.save(this.context, location);
        }

        return report;
    }

    public ForecastReport getReport(ILocation location) throws IOException, JSONException {
        return getReport(location, defaultMinDate());
    }

    static Date defaultMinDate() {
        // 3 hours
        return new Date(new Date().getTime() - 1000 * 60 * 60 * 3);
    }
}
