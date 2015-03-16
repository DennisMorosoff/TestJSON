package com.example.danner.testjson;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AsyncHandleShedJSON extends AsyncTask<ActionBarActivity, Integer, SimpleAdapter> {
    private String times[];
    private String dates[] = new String[6];
    private String titles[];
    private String lecturers[];
    private String rooms[];
    private MainActivity mMainActivity;
    private String urlString;

    @Override
    protected SimpleAdapter doInBackground(ActionBarActivity... params) {

        SimpleAdapter mShedSimpleAdapter = null;

        mMainActivity = (MainActivity) params[0];

        int Course = mMainActivity.mSpinnerCourse.getSelectedItemPosition() + 1;

        switch (Course) {
            case 1:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDUWXJqZ2xiTnJ5N2c";
                break;
            case 2:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDUNy1mc1g0dEFvc0U";
                break;
            case 3:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDURGlSZzN3SjZuQWs";
                break;
            case 4:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDUUFhXNGxnUk9zNm8";
                break;
            case 5:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDUZzV4am5yeXNONlU";
                break;
            default:
                urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=0B8QN8De7yDDUWXJqZ2xiTnJ5N2c";
                break;
        }

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

            Log.d(MainActivity.myLogs, "conn.connect finish, conn.getResponseCode: " + conn.getResponseCode());

            InputStream stream = conn.getInputStream();

            Log.d(MainActivity.myLogs, "stream: " + stream);

            String data = convertStreamToString(stream);

            Log.d(MainActivity.myLogs, "convertStreamToString(stream), data: " + data);

            int index = mMainActivity.mSpinnerGroup.getSelectedItemPosition();

            JSONArray main = new JSONArray(data);

            JSONObject group = main.getJSONObject(index);

            JSONArray weekdays = group.getJSONArray("weekday");

            JSONObject monday = weekdays.getJSONObject(0);

            JSONArray weeks = monday.getJSONArray("week");

            JSONObject monday0 = weeks.getJSONObject(0);

            JSONArray lessons = monday0.getJSONArray("lessonweek");

            times = new String[lessons.length()];
            titles = new String[lessons.length()];
            lecturers = new String[lessons.length()];
            rooms = new String[lessons.length()];

            for (int i = 0; i < lessons.length(); i++) {
                times[i] = " ";
                titles[i] = " ";
                lecturers[i] = " ";
                rooms[i] = " ";
            }

            for (int i = 0; i < lessons.length(); i++) {

                JSONObject lesson = lessons.getJSONObject(i);
                times[i] = lesson.getString("time");
                titles[i] = lesson.getString("lesson");
                lecturers[i] = lesson.getString("lecturer");
                rooms[i] = lesson.getString("room");
            }

            ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            Log.d(MainActivity.myLogs, "myArrList: " + myArrList);

            for (int i = 0; i < times.length; i++) {
                map = new HashMap<String, String>();
                map.put("times", times[i]);
                map.put("titles", titles[i]);
                map.put("lecturers", lecturers[i]);
                map.put("rooms", rooms[i]);

                Log.d(MainActivity.myLogs, "map: " + map + ", i = " + i);

                myArrList.add(map);
            }

            mShedSimpleAdapter = new SimpleAdapter(mMainActivity.getApplicationContext(), myArrList, R.layout.fragment_shed_item_list,
                    new String[]{"times", "titles", "lecturers", "rooms"},
                    new int[]{R.id.textShedTime, R.id.textShedTitle, R.id.textShedLecturer, R.id.textShedRoom});

            Log.d(MainActivity.myLogs, "mShedSimpleAdapter: " + mShedSimpleAdapter);

            stream.close();

            Log.d(MainActivity.myLogs, "stream.close");

            conn.disconnect();

            Log.d(MainActivity.myLogs, "conn.disconnect");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mShedSimpleAdapter;
    }

    @Override
    protected void onPostExecute(SimpleAdapter listAdapter) {

        Log.d(MainActivity.myLogs, "onPostExecute, listAdapter: " + listAdapter);

        super.onPostExecute(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute, mMainActivity.mListView: " + mMainActivity.mListView);

        mMainActivity.mListView.setAdapter(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute finish");

    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

