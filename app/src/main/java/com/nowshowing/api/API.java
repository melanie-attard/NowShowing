package com.nowshowing.api;

import com.nowshowing.models.Season;
import com.nowshowing.models.Show;
import com.nowshowing.models.DetailedShow;
import com.nowshowing.models.Episode;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    String BASE_URL = "https://api.tvmaze.com/";
    @GET("shows?page=2") // retrieves a list of 250 shows
    Call<List<Show>> getShows();

    @GET("shows/{id}")
    Call<DetailedShow> getShowDetails(@Path("id") int Id);

    @GET("shows/{id}/episodebynumber")
    Call<Episode> getEpisode(@Path("id") int ShowId, @Query("season") int Season, @Query("number") int Number);

    @GET("shows/{id}/seasons")
    Call<List<Season>> getSeasons(@Path("id") int Id);

    @GET("seasons/{id}/episodes") // get list of episodes by season
    Call<List<Episode>> getEpisodesBySeason(@Path("id") int Id);
}
