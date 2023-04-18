package com.nowshowing.api;

import com.nowshowing.Show;
import com.nowshowing.detailsFragments.DetailedShow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API {
    String BASE_URL = "https://api.tvmaze.com/";
    @GET("shows?page=1") // retrieves a list of 250 shows
    Call<List<Show>> getShows();

    @GET("shows/{id}")
    Call<DetailedShow> getShowDetails(@Path("id") int Id);

    // try @GET("shows/{id}/episodes") for list of episodes
}
