package com.nowshowing;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("original")
    public String img;

    public Image(String img) {
        this.img = img;
    }

    public String getImgUrl() {
        return img;
    }

    public void setImgUrl(String img) {
        this.img = img;
    }
}
