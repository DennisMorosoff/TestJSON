package com.example.danner.testjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.util.Log;

public class HandleJSON {
    private String country = "county";
    private String temperature = "temperature";
    private String humidity = "humidity";
    private String pressure = "pressure";
    private String text = "text";
    private String date = "date";
    private String urlString = null;

    public volatile boolean parsingComplete = true;

    public HandleJSON(String url) {
        this.urlString = url;
    }

    public String getCountry() {
        return country;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            JSONObject sys = reader.getJSONObject("sys");
            country = sys.getString("country");

            JSONObject main = reader.getJSONObject("main");
            temperature = main.getString("temp");

            pressure = main.getString("pressure");
            humidity = main.getString("humidity");

            parsingComplete = false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    public void readAndParseVK(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            Log.v("myLogs", "reader: " + reader);

            JSONArray response = reader.getJSONArray("response");

            Log.v("myLogs", "response: " + response);

            JSONObject pages = response.getJSONObject(1);

            Log.v("myLogs", "pages: " + pages);

            text = pages.getString("text");

            Log.v("myLogs", "text: " + text);

            date = pages.getString("date");

            Log.v("myLogs", "date: " + date);

            parsingComplete = false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void fetchJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);

                    readAndParseVK(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}