package com.example.myblogapp.http;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Blog implements Parcelable {
    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy"); // 1

    @PrimaryKey
    private int id;
    @Embedded
    private Author author;
    private String title;
    private String date;
    private String image;
    private String description;
    private int views;
    private float rating;

    public Blog() {
    }

    protected Blog(Parcel in) {
        id = Integer.parseInt(in.readString());
        author = in.readParcelable(Author.class.getClassLoader());
        title = in.readString();
        date = in.readString();
        image = in.readString();
        description = in.readString();
        views = in.readInt();
        rating = in.readFloat();
    }

    public Long getDateMilliseconds() {
        try {
            Date date = dateFormat.parse(getDate());
            return date != null ? date.getTime() : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }

    public float getRating() {
        return rating;
    }

    public String getImageURL() {
        return BlogHttpClient.BASE_URL + BlogHttpClient.PATH + getImage();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(id));
        parcel.writeParcelable(author, i);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeInt(views);
        parcel.writeFloat(rating);
    }
}
