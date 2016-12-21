package com.ournet.weather.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
    public static JSONObject call(String urlString, String method, HashMap<String, String> properties, byte[] postData) throws IOException, JSONException {

        Log.i("data", "Connecting to " + urlString + " - " + method);
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Accept", "*/*" );
        conn.setRequestMethod(method);
        conn.setReadTimeout(20000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        conn.setDoOutput(true);

        if (postData != null) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData, 0, postData.length);
            wr.flush();
            wr.close();
        }

//        conn.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

//        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        conn.disconnect();

        jsonString = sb.toString();

        Log.i("data", "JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

    public static JSONObject get(String url) throws IOException, JSONException {
        return JsonClient.call(url, "GET", null, null);
    }

    public static JSONObject post(String url, HashMap<String, String> properties, byte[] postData) throws IOException, JSONException {
        return JsonClient.call(url, "POST", properties, postData);
    }
}
