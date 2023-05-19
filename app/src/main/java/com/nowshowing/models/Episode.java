package com.nowshowing.models;

import com.google.gson.annotations.SerializedName;
import com.nowshowing.models.Image;

import java.util.Date;

public class Episode {
    @SerializedName("id")
    private int epId;
    @SerializedName("name")
    private String title;

    @SerializedName("season")
    private int season;

    @SerializedName("number")
    private int epNum;

    @SerializedName("airdate")
    private Date date;

    @SerializedName("image")
    private Image image;

    public Episode(int id, String title, int season, int epNum, Date date, Image image) {
        epId = id;
        this.title = title;
        this.season = season;
        this.epNum = epNum;
        this.date = date;
        this.image = image;
    }

    public int getEpId() {
        return epId;
    }

    public void setEpId(int epId) {
        this.epId = epId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpNum() {
        return epNum;
    }

    public void setEpNum(int epNum) {
        this.epNum = epNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
