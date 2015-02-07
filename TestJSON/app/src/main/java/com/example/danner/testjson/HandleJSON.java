package com.example.danner.testjson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class HandleJSON {
    private String texts[] = new String[20];
    private String dates[] = new String[20];
    private String urlString = "https://api.vk.com/method/wall.get?owner_id=-30617342";
    ActionBarActivity mActivity;

    public volatile boolean parsingComplete = true;

    public HandleJSON(ActionBarActivity activity) {

        Log.d(MainActivity.myLogs, "HandleJSON starts, activity: " + activity);

        this.mActivity = activity;

        Log.d(MainActivity.myLogs, "HandleJSON finish, mActivity: " + this.mActivity);

    }

    public ListAdapter getVKListAdapter() {

        Log.d(MainActivity.myLogs, "getVKListAdapter starts, texts: " + texts + ", texts[1]: " + texts[1]);

        ListAdapter mVKListAdapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_list_item_1, android.R.id.text1, texts);

        Log.d(MainActivity.myLogs, "mVKListAdapter: " + mVKListAdapter);

        return mVKListAdapter;
    }

    public void readAndParseVK(String in) {
        try {

            Log.d(MainActivity.myLogs, "readAndParseVK starts, in: " + in);

            JSONObject reader = new JSONObject(in);

            Log.d(MainActivity.myLogs, "reader: " + reader);

            JSONArray response = reader.getJSONArray("response");

            Log.d(MainActivity.myLogs, "response: " + response);

            for (int i = 1; i < 4; i++) {
                JSONObject JSONpage = response.getJSONObject(i);
                texts[i] = JSONpage.getString("text");
                dates[i] = JSONpage.getString("date");
            }

            Log.d(MainActivity.myLogs, "texts: " + texts + ", texts[1]: " + texts[1]);

            parsingComplete = false;

            Log.d(MainActivity.myLogs, "readAndParseVK finish, parsingComplete: " + parsingComplete);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void fetchJSON() {
        try {

            Log.d(MainActivity.myLogs, "fetchJSON starts");

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

            Log.d(MainActivity.myLogs, "convertStreamToString(stream), data: " + data);

            readAndParseVK(data);

            Log.d(MainActivity.myLogs, "readAndParseVK");

            stream.close();

            Log.d(MainActivity.myLogs, "stream.close");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}