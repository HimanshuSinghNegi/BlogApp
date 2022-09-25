package com.example.myblogapp.http;

import java.util.List;

public interface BlogArticlesCallBack {
    void onSuccess(List<Blog> blogList);
    void onError();
}
