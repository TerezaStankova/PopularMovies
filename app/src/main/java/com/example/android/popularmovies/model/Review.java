package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

        private String author;
        private String content;

    //Constructor for reviews
    public Review() {
        }

    public Review(String author, String content) {
            this.author = author;
            this.content = content;
        }

        public String getReviewAuthor() {
            return author;
        }
        public String getReviewContent() {
            return content;
        }

        public void setReviewAuthor(String name) {
            this.author = name;
        }
        public void setReviewContent(String key) {
            this.content = key;
        }



    private Review(Parcel in){
            author = in.readString();
            content = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(author);
            parcel.writeString(content);
        }

        public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel parcel) {
                return new Review(parcel);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }

        };
    }