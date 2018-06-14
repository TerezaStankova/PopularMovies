package com.example.android.popularmovies.model;



import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String title;
    private String originalTitle;
    private String releaseDate;
    private double voteAverage;
    private String poster;
    private String plot;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(String title, String originalTitle, String releaseDate, double voteAverage, String poster, String plot) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.poster = poster;
        this.plot = plot;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }


    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }


    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }


    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {this.plot = plot;}

    private Movie(Parcel in){
        title = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        poster = in.readString();
        plot = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
        parcel.writeString(poster);
        parcel.writeString(plot);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };
}
