package com.ournet.weather.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 12/20/16.
 */

public class JsonClient {

    private static JSONObject callPost(String urlString, HashMap<String, String> properties, String postData) throws IOException, JSONException {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setConnectTimeout(3000);
            connection.setReadTimeout(5000);

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            if (properties != null) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            Log.i("data", postData);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            String jsonString = response.toString();
//            Log.i("data", "JSON: " + jsonString);
            return new JSONObject(jsonString);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    public static JSONObject call(String urlString, String method, HashMap<String, String> properties, String postData) throws IOException, JSONException {

        Log.i("data", "Connecting to " + urlString + " - " + method);

        if (method == "POST") {
            return callPost(urlString, properties, postData);
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestMethod(method);
            conn.setReadTimeout(20000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            if (properties != null) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.setDoOutput(true);

            conn.connect();

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

//            Log.i("data", "JSON: " + jsonString);

            return new JSONObject(jsonString);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static JSONObject get(String url) throws IOException, JSONException {
        return JsonClient.call(url, "GET", null, null);
    }

    public static JSONObject post(String url, HashMap<String, String> properties, String postData) throws IOException, JSONException {
        return JsonClient.call(url, "POST", properties, postData);
    }
}
