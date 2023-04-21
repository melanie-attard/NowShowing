package com.nowshowing.models;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("average")
    private double averageRating;

    public Rating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
