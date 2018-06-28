package com.example.android.popularmovies.model;



import android.os.Parcel;
import android.os.Parcelable;

/**Implementing Parcelable for this model object
 * - it can be passed across Activities and Fragments */
public class Movie implements Parcelable{
    private String title;
    private String originalTitle;
    private String releaseDate;
    private double voteAverage;
    private String poster;
    private String plot;
    private int id;

    /* No args constructor */
    public Movie() {
    }

    public Movie(String title, String originalTitle, String releaseDate, double voteAverage, String poster, String plot, int id) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.poster = poster;
        this.plot = plot;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getVoteAverage() {
        return Double.toString(voteAverage);
    }
    public String getPoster() {
        return poster;
    }
    public String getPlot() {
        return plot;
    }
    public int getId() {
        return id;
    }

    private Movie(Parcel in){
        title = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        poster = in.readString();
        plot = in.readString();
        id = in.readInt();
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
        parcel.writeInt(id);
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
