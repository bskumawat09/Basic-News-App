package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String KEY = "f2d113784d514c71977e9faca";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsRecyclerView = findViewById(R.id.recycler_view);

        mNewsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<>());

        newsRecyclerView.setAdapter(mNewsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    void loadData() {

        Request req = new Request.Builder().url("https://newsapi.org/v2/top-headlines?country=in&apiKey=" + KEY).build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("unexpected code " + response);
                }

                String responseString = response.body().string();
                Log.d(LOG_TAG, responseString);
                try {
                    ArrayList<News> newsList = QueryUtil.extractNewsFromJSON(responseString);
                    if (newsList != null && !newsList.isEmpty()) {

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNewsAdapter.update(newsList);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(LOG_TAG, "request failed");
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}