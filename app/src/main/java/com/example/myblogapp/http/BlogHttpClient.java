package com.example.myblogapp.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class BlogHttpClient {
    public static final BlogHttpClient INSTANCE = new BlogHttpClient();
    //    private static final String BASE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/";
//    private static final String BLOG_ARTICLES_URL = BASE_URL + "647f4270e4271fbff28f1d80e2f2d12b3bd4a1cd/blog_articles.json";
    public static final String BASE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources";
    public static final String PATH = "/raw/3eede691af3e8ff795bf6d31effb873d484877be";
    private static final String BLOG_ARTICLES_URL = BASE_URL + PATH + "/blog_articles.json";
    private final Executor executor;
    private final OkHttpClient client;
    private final Gson gson;

    private BlogHttpClient() {
        executor = Executors.newFixedThreadPool(4);
        client = new OkHttpClient();
        gson = new Gson();
    }

//    public void loadBlogArticles(BlogArticlesCallBack callBack) {
//        Request request = new Request.Builder() //1
//                .get()
//                .url(BLOG_ARTICLES_URL).build();
//        //2
//        executor.execute(() -> {
//            try {
//                Response response = client.newCall(request).execute(); //3
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {//4
//                    String json = responseBody.string();
//                    BlogData blogData = gson.fromJson(json, BlogData.class);
//                    if (blogData != null) {
//                        //success blogData.getData();
//                        callBack.onSuccess(blogData.getData());
//                        return;
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//            callBack.onError();
//        });
//    }


    public List<Blog> loadBlogArticles(){
        Request request = new Request.Builder()
                .get()
                .url(BLOG_ARTICLES_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String json = responseBody.string();
                BlogData blogData = gson.fromJson(json, BlogData.class);
                if (blogData != null) {
                    return blogData.getData();
                }
            }
        } catch (IOException e) {
            Log.e("BlogHttpClient", "Error loading blog articles", e);
        }
        return null;
    }



}
