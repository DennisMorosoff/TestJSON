package com.example.danner.testjson;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danner on 07.02.2015.
 */
public class AsyncHandleJSON extends AsyncTask<ActionBarActivity, Integer, SimpleAdapter> {
    private String texts[] = new String[19];
    private String dates[] = new String[19];
    private MainActivity mMainActivity;
    private String urlString = "http://api.vk.com/method/wall.get?owner_id=-30617342";

    @Override
    protected SimpleAdapter doInBackground(ActionBarActivity... params) {
        ListAdapter mVKListAdapter = null;

        SimpleAdapter mVKSimpleAdapter = null;

        mMainActivity = (MainActivity) params[0];

        Log.d(MainActivity.myLogs, "mMainActivity: " + mMainActivity);

        try {

            Log.d(MainActivity.myLogs, "fetchJSON starts, urlString: " + urlString);

            URL url = new URL(urlString);

            Log.d(MainActivity.myLogs, "url: " + url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(MainActivity.myLogs, "conn: " + conn);

            conn.setReadTimeout(100000);
            Log.d(MainActivity.myLogs, "1");
            conn.setConnectTimeout(100000);
            Log.d(MainActivity.myLogs, "2");
            conn.setRequestMethod("GET");
            Log.d(MainActivity.myLogs, "3");
            conn.setDoInput(true);
//            conn.connect();

            Log.d(MainActivity.myLogs, "conn.connect finish, conn.getResponseCode: " + conn.getResponseCode());

            InputStream stream = conn.getInputStream();

            Log.d(MainActivity.myLogs, "stream: " + stream);

            String data = convertStreamToString(stream);

            Log.d(MainActivity.myLogs, "convertStreamToString(stream), data: " + data);

            JSONObject reader = new JSONObject(data);

            Log.d(MainActivity.myLogs, "reader: " + reader);

            JSONArray response = reader.getJSONArray("response");

            Log.d(MainActivity.myLogs, "response: " + response);

            for (int i = 0; i < texts.length; i++) {
                texts[i] = " ";
                dates[i] = " ";
            }

            for (int i = 0; i < texts.length; i++) {
                JSONObject JSONpage = response.getJSONObject(i + 2);
                texts[i] = JSONpage.getString("text");
                dates[i] = JSONpage.getString("date");
            }

            Log.d(MainActivity.myLogs, "texts: " + texts + ", texts[0]: " + texts[0]);

            ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < texts.length; i++) {
                map = new HashMap<String, String>();
                map.put("date", dates[i]);
                map.put("text", texts[i]);
                myArrList.add(map);
            }

            mVKSimpleAdapter = new SimpleAdapter(mMainActivity.getApplicationContext(), myArrList, R.layout.fragment_vk_news_item_list,
                    new String[] {"date", "text"},
                    new int[] {R.id.textTitle, R.id.textContent});

            mVKListAdapter = new ArrayAdapter<String>(mMainActivity,
                    android.R.layout.simple_list_item_1, android.R.id.text1, texts);

            Log.d(MainActivity.myLogs, "mVKListAdapter: " + mVKListAdapter);

            stream.close();

            Log.d(MainActivity.myLogs, "stream.close");

            conn.disconnect();

            Log.d(MainActivity.myLogs, "conn.disconnect");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mVKSimpleAdapter;
    }

    @Override
    protected void onPostExecute(SimpleAdapter listAdapter) {

        Log.d(MainActivity.myLogs, "onPostExecute, listAdapter: " + listAdapter);

        super.onPostExecute(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute, mMainActivity.mListView: " + mMainActivity.mListView);

        mMainActivity.mListView.setAdapter(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute finish");

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

