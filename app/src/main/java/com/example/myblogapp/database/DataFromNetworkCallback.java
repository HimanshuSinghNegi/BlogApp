package com.example.myblogapp.database;

import com.example.myblogapp.http.Blog;

import java.util.List;

public interface DataFromNetworkCallback {
    void onSuccess(List<Blog> blogList);
    void onError();
}
