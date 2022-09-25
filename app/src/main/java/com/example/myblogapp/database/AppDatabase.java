package com.example.myblogapp.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.myblogapp.http.Blog;

@Database(entities = {Blog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BlogDAO blogDao();
}
