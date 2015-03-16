package com.example.danner.testjson;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

public class AsyncHandleGroupNameJSON extends AsyncTask<ActionBarActivity, Integer, ArrayAdapter> {
    private MainActivity mMainActivity;
    private String urlString;

    @Override
    protected ArrayAdapter doInBackground(ActionBarActivity... params) {


        ArrayAdapter<String> adapter = null;

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

            //    JSONObject reader = new JSONObject(data);


            JSONArray main = new JSONArray(data);

            String[] nameGroups = new String[main.length()];

            for (int i = 0; i < main.length(); i++) {
                JSONObject group = main.getJSONObject(i);
                String nameGroup = group.getString("NameGroup");
                nameGroups[i] = nameGroup;
            }

            adapter = new ArrayAdapter<String>(mMainActivity.getApplicationContext(), R.layout.spinner_item, nameGroups);


            //     Spinner mSpinner = (Spinner) mMainActivity.findViewById(R.id.spinner);

            //        mSpinner.setAdapter(adapter);

            //      Log.d(MainActivity.myLogs, "reader: " + reader);

            //   JSONArray response = main.getJSONArray("response");

            /*     Log.d(MainActivity.myLogs, "response: " + response);

            for (int i = 0; i < texts.length; i++) {
                texts[i] = " ";
                dates[i] = " ";
                photos[i] = " ";
                titles[i] = " ";
                source[i] = " ";
            }

            for (int i = 0; i < texts.length; i++) {
                JSONObject JSONpage = response.getJSONObject(i + 2);
                texts[i] = JSONpage.getString("text");
                dates[i] = JSONpage.getString("date");

                String htmltext = Html.fromHtml(texts[i]).toString();
                texts[i] = htmltext;

                if (JSONpage.getString("post_type").equals("copy")) {
                    if (JSONpage.has("copy_owner_id")) {
                        source[i] = JSONpage.getString("copy_owner_id");
                    }
                }

                long time = Integer.valueOf(dates[i]) * (long) 1000;
                Date date = new Date(time);
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMMM в HH:mm");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                dates[i] = format.format(date);

                if (JSONpage.has("attachment")) {
                    JSONObject JSONpageAttachment = JSONpage.getJSONObject("attachment");

                    if (JSONpageAttachment.getString("type").equals("photo")) {
                        JSONObject JSONpagePhoto = JSONpageAttachment.getJSONObject("photo");
                        if (JSONpagePhoto.has("src")) {
                            photos[i] = JSONpagePhoto.getString("src");
                        }
                    }
                    if (JSONpageAttachment.getString("type").equals("link")) {
                        JSONObject JSONpageLink = JSONpageAttachment.getJSONObject("link");
                        if (JSONpageLink.has("title")) {
                            titles[i] = JSONpageLink.getString("title");
                        }
                    }
                }
            }

            Log.d(MainActivity.myLogs, "Массивы заполнены");

            ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            Log.d(MainActivity.myLogs, "myArrList: " + myArrList);

            for (int i = 0; i < texts.length; i++) {
                map = new HashMap<String, String>();
                map.put("title", titles[i]);
                map.put("date", dates[i]);
                map.put("text", texts[i]);
                map.put("image", photos[i]);
                map.put("source", source[i]);

                Log.d(MainActivity.myLogs, "map: " + map + ", i = " + i);

                myArrList.add(map);
            }

            mVKSimpleAdapter = new SimpleAdapter(mMainActivity.getApplicationContext(), myArrList, R.layout.fragment_vk_news_item_list,
                    new String[]{"title", "text", "date", "image", "source"},
                    new int[]{R.id.textTitle, R.id.textContent, R.id.dateTitle, R.id.imgContext, R.id.imgTitle});

            Log.d(MainActivity.myLogs, "mVKSimpleAdapter: " + mVKSimpleAdapter);

            stream.close();

            Log.d(MainActivity.myLogs, "stream.close");

            conn.disconnect();

            Log.d(MainActivity.myLogs, "conn.disconnect");*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        return adapter;
    }

    @Override
    protected void onPostExecute(ArrayAdapter listAdapter) {

        Log.d(MainActivity.myLogs, "onPostExecute, listAdapter: " + listAdapter);

        super.onPostExecute(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute, mMainActivity.mListView: " + mMainActivity.mListView);

        mMainActivity.mSpinnerGroup.setAdapter(listAdapter);

        Log.d(MainActivity.myLogs, "onPostExecute finish");

    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

