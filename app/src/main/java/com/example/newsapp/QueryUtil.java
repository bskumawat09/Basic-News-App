package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.internal.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryUtil {
    private QueryUtil() {
    }

    public static ArrayList<News> extractNewsFromJSON(String responseString) {

        if (TextUtils.isEmpty(responseString)) {
            return null;
        }

        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject responseJSON = new JSONObject(responseString);

            JSONArray articleArray = responseJSON.getJSONArray("articles");

            for (int i = 0; i < articleArray.length(); i++) {
                JSONObject currentObject = articleArray.getJSONObject(i);

                String source = currentObject.getJSONObject("source").getString("name");
                String title = currentObject.getString("title");
                String author = currentObject.getString("author");
                String url = currentObject.getString("url");
                String time = currentObject.getString("publishedAt");
                String urlImage = currentObject.getString("urlToImage");

                News newsObject = new News(author, source, time, title, url, urlImage);

                newsList.add(newsObject);
            }

        } catch (JSONException e) {
            Log.e("QueryUtil", "Problem parsing the news JSON response", e);
        }
        return newsList;
    }
}
