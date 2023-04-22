package com.nowshowing.models;

import com.google.gson.annotations.SerializedName;

public class Season {
    @SerializedName("id")
    private int sId;
    @SerializedName("number")
    private int num;

    @SerializedName("episodeOrder")
    private int epCount;

    public Season(int num, int epCount, int sId) {
        this.num = num;
        this.epCount = epCount;
        this.sId = sId;
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

    public int getSeasonId() {
        return sId;
    }

    public void setSeasonId(int sId) {
        this.sId = sId;
    }
}
