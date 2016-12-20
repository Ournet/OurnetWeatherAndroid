package com.ournet.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 12/20/16.
 */

public class JsonClient {
    public static JSONObject call(String urlString, String method, HashMap<String, String> properties) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        if(properties!=null) {
            for(Map.Entry<String, String> entry : properties.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

    public static JSONObject get(String url) throws IOException, JSONException {
        return JsonClient.call(url, "GET", null);
    }

    public static JSONObject post(String url, HashMap<String, String> properties) throws IOException, JSONException {
        return JsonClient.call(url, "POST", properties);
    }
}
