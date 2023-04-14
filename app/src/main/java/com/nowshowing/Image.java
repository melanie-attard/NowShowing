package com.nowshowing;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("original")
    public String img;

    public Image(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
