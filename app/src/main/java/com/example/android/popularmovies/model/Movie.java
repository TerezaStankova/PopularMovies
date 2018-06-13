package com.example.android.popularmovies.model;

public class Movie {
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
}
