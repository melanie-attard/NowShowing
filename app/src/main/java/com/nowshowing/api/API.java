package com.nowshowing.api;

import com.nowshowing.Show;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    String BASE_URL = "https://api.tvmaze.com/";
    @GET("shows?page=1") // retrieves a list of 250 shows
    Call<List<Show>> getShows();
}
