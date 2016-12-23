package com.ournet.weather;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Dumitru Cantea on 12/23/16.
 */

public class Links {
    public static class Weather {
        private static final HashMap<String, String> hosts = new HashMap();

        static {
            hosts.put("ro", "meteo.ournet.ro");
            hosts.put("md", "meteo.click.md");
            hosts.put("ru", "pogoda.zborg.ru");
            hosts.put("bg", "vremeto.ournet.bg");
            hosts.put("pl", "pogoda.diez.pl");
            hosts.put("cz", "pocasi.ournet.ro");
            hosts.put("hu", "idojaras.ournet.hu");
            hosts.put("it", "meteo.ournet.it");
            hosts.put("in", "weather.ournet.in");
            hosts.put("al", "www.moti2.al");
        }

        public static String home(String country, HashMap<String, String> props) {
            return format(country, "/", props);
        }

        public static String home(String country) {
            return home(country, null);
        }

        public static String place(String country, int id, HashMap<String, String> props) {
            return format(country, "/" + id, props);
        }

        public static String place(String country, int id) {
            return place(country, id, null);
        }

        public static String home(String country, int id) {
            return format(country, "/" + id, null);
        }

        private static String format(String country, String link, HashMap<String, String> props) {
            String host = Weather.hosts.get(country.toLowerCase());

            if (host == null) {
                return null;
            }

            link = "http://" + host + link;

            return setProps(link, props);
        }
    }

    private static String setProps(String link, HashMap<String, String> props) {
        if (props == null || props.size() == 0) {
            return link;
        }

        link += "?";
        for (String key : props.keySet()) {
            link += key + "=" + props.get(key) + "&";
        }
        link = link.substring(0, link.length() - 1);

        return link;
    }
}
