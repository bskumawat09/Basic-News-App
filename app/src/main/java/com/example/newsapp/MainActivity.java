package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final static String KEY = "f2d113784d514c71977e9faca";

    private TextView mEmptyStateTextView;
    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

        RecyclerView newsRecyclerView = findViewById(R.id.recycler_view);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.VISIBLE);

        mNewsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<>());

        newsRecyclerView.setAdapter(mNewsAdapter);
        newsRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));

        loadData();
    }

    private void loadData() {
        NetworkInfo networkInfo = getNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchData();
            mEmptyStateTextView.setVisibility(View.GONE);
        } else {
            // Otherwise, display error
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
            Toast.makeText(this, "Check Your Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchData() {
        Request req = new Request.Builder().url("https://newsapi.org/v2/top-headlines?country=in&apiKey=" + KEY).build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onResponse(final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, "No Response");

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEmptyStateTextView.setText(R.string.no_response);
                            Toast.makeText(MainActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                String responseString = response.body().string();
                Log.d(LOG_TAG, responseString);

                ArrayList<News> newsList = QueryUtil.extractNewsFromJSON(responseString);

                if (newsList != null && !newsList.isEmpty()) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEmptyStateTextView.setVisibility(View.GONE);
                            mNewsAdapter.update(newsList);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(LOG_TAG, "Request Failed");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyStateTextView.setText(R.string.request_failed);
                        Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void refreshNews(View view) {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(MainActivity.this, "Feed Refreshed", Toast.LENGTH_SHORT).show();
        }
        loadData();
    }

    public NetworkInfo getNetworkInfo() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo;
    }
}