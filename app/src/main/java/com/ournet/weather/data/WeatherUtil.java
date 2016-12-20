package com.ournet.weather.data;

/**
 * Created by Dumitru Cantea on 12/20/16.
 */

public class WeatherUtil {
    public static String[] SupportedCountries = new String[]{"md", "ro", "ru", "bg", "in", "hu", "cz", "al", "pl"};

    public static String getCountryByLanguage(String language) {
        switch (language.toLowerCase()) {
            case "en":
                return "in";
            case "cs":
                return "cz";
        }

        return language;
    }
}
