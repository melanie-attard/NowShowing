package com.nowshowing.models;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    @SerializedName("show")
    private Show show;

    public SearchResult(Show show) {
        this.show = show;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
