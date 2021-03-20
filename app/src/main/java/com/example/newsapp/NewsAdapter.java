package com.example.newsapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private final String LOG_TAG = NewsAdapter.class.getSimpleName();
    private final ArrayList<News> mNewsList;
    private final Context context;

    // NewsAdapter constructor
    public NewsAdapter(Context ctx, ArrayList<News> newsList) {
        mNewsList = newsList;
        context = ctx;
    }

    // Updates the mNewsList
    public void update(ArrayList<News> newsList) {
        mNewsList.clear();
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "getItemCount");
        return mNewsList.size();
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new NewsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder");
        News currentNews = mNewsList.get(position);

        if (currentNews.getUrlImage() == null) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(currentNews.getUrlImage()).into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
        }

        holder.titleView.setText(currentNews.getTitle());
        holder.sourceView.setText(currentNews.getSource());
        holder.timeView.setText(currentNews.getTime());
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView sourceView;
        private final TextView timeView;

        // ViewHolder constructor
        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            titleView = itemView.findViewById(R.id.title_view);
            sourceView = itemView.findViewById(R.id.source_view);
            timeView = itemView.findViewById(R.id.time_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked
            int position = getLayoutPosition();
            // Use that to access the affected item in mNewsList
            News currentNews = mNewsList.get(position);

            String url = currentNews.getUrlString();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }
}