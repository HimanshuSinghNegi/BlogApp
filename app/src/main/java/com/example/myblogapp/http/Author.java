package com.example.myblogapp.http;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {
    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
    private String name;
    private String avatar;

    public Author() {
    }

    public Author(Parcel in) {
        name = in.readString();
        avatar = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarURL() {
        return BlogHttpClient.BASE_URL + BlogHttpClient.PATH + getAvatar();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(avatar);
    }
}
