package com.nowshowing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Show {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("genres")
    private ArrayList<String> genres;

    @SerializedName("image")
    private Image imageURL;
    // inspired by
    // https://stackoverflow.com/questions/42433276/how-to-parse-json-object-with-gson-based-on-dynamic-serializedname-in-android

    public Show(int id, String name, ArrayList<String> genres, Image imageURL) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Image getImages() {
        return imageURL;
    }

    public void setImages(Image imageURL) {
        this.imageURL = imageURL;
    }
}