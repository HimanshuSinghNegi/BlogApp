package com.example.myblogapp.database;

import com.example.myblogapp.http.Blog;

import java.util.List;

public interface DataFromDatabaseCallback {
    void onSuccess(List<Blog> blogList);
}
