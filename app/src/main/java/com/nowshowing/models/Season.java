package com.nowshowing.models;

import com.google.gson.annotations.SerializedName;

public class Season {
    @SerializedName("number")
    private int num;

    @SerializedName("episodeOrder")
    private int epCount;

    public Season(int num, int epCount) {
        this.num = num;
        this.epCount = epCount;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getEpCount() {
        return epCount;
    }

    public void setEpCount(int epCount) {
        this.epCount = epCount;
    }
}
