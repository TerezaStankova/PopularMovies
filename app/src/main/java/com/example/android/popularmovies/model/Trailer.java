package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable{
    private String name;
    private String key;

    /**
     * No args constructor for use in serialization
     */
    public Trailer() {
    }

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getTrailerName() {
        return name;
    }
    public String getTrailerKey() {
        return key;
    }

    public void setTrailerName(String name) {
        this.name = name;
    }
    public void setTrailerKey(String key) {
        this.key = key;
    }



    private Trailer(Parcel in){
        name = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(key);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }

    };
}