package com.example.newsapp;

public class News {

    private String mSource;
    private String mAuthor;
    private String mTime;
    private String mTitle;
    private String mUrlString;
    private String mUrlImage;

    public News(String author, String source, String time, String title, String urlString, String urlImage) {
        mAuthor = author;
        mSource = source;
        mTime = time;
        mTitle = title;
        mUrlString = urlString;
        mUrlImage = urlImage;
    }

//    public News(String author, String source, String time, String title, String urlString) {
//        mAuthor = author;
//        mSource = source;
//        mTime = time;
//        mTitle = title;
//        mUrlString = urlString;
//    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSource() {
        return mSource;
    }

    public String getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrlString() {
        return mUrlString;
    }

    public String getUrlImage() {
        return mUrlImage;
    }
}
