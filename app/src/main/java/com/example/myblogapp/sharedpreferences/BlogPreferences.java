package com.example.myblogapp.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class BlogPreferences {
    private static final String KEY_LOGIN_STATE = "key_login_state";
    private final SharedPreferences sharedPreferences;

    public BlogPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("travel-blog", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit().putBoolean(KEY_LOGIN_STATE, loggedIn).apply();
    }
}
