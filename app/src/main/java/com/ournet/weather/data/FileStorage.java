package com.ournet.weather.data;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dumitru Cantea on 12/22/16.
 */

public class FileStorage {
    public static boolean save(Context context, String name, String data) throws IOException {
        name = name + ".json";
        File file = new File(context.getFilesDir(), name);
        FileOutputStream is = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(is);
        Writer w = new BufferedWriter(osw);
        w.write(data);
        w.close();

        return true;
    }

    public static String load(Context context, String name) throws IOException {
        name = name + ".json";
        File file = new File(context.getFilesDir(), name);
        StringBuilder text = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        return text.toString();
    }

    public static boolean save(Context context, String name, JSONObject data) throws IOException {
        return FileStorage.save(context, name, data.toString());
    }

    public static JSONObject loadJson(Context context, String name) throws IOException, JSONException {
        String json = FileStorage.load(context, name);
        return new JSONObject(json);
    }
}
