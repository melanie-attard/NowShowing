package com.nowshowing.detailsFragments;

import com.google.gson.annotations.SerializedName;
import com.nowshowing.Image;

import java.util.ArrayList;

public class DetailedShow {
    @SerializedName("name")
    private String name;

    @SerializedName("genres")
    private ArrayList<String> genres;

    @SerializedName("image")
    private Image imageURL;

    @SerializedName("status")
    private String status;

    @SerializedName("summary")
    private String description;

    @SerializedName("rating")
    private Rating rating;

    public DetailedShow(String name, ArrayList<String> genres, Image imageURL, String status, String description, Rating rating) {
        this.name = name;
        this.genres = genres;
        this.imageURL = imageURL;
        this.status = status;
        this.description = description;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public Image getImageURL() {
        return imageURL;
    }

    public void setImageURL(Image imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
