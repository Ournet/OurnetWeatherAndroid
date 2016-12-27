package com.ournet.weather;

import com.ournet.weather.data.ForecastReport;

/**
 * Created by Dumitru Cantea on 12/27/16.
 */

public interface OnForecastReportChanged {
    void onFirecastReportChanged(ForecastReport report);
}
