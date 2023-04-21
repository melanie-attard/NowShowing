package com.nowshowing.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nowshowing.Show;
import com.nowshowing.detailsFragments.DetailedShow;
import com.nowshowing.detailsFragments.Episode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestRepository {
    private static RestRepository instance = null;
    private API api;

    private RestRepository() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static synchronized RestRepository getInstance() {
        if (instance == null)
        {
            instance = new RestRepository();
        }
        return instance;
    }

    public LiveData<List<Show>> fetchShows() {
        final MutableLiveData<List<Show>> users = new MutableLiveData<>();

        api.getShows().enqueue(new Callback<List<Show>>() {
            @Override
            public void onResponse(@NonNull Call<List<Show>> call, @NonNull Response<List<Show>> response) {
                if (!response.isSuccessful()) { return; }
                users.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Show>> call, @NonNull Throwable t) {
                Log.i("fetchShows", call.request().toString());
                Log.e("fetchShows", t.getMessage());
                users.setValue(null);
            }
        });
        return users;
    }

    public LiveData<DetailedShow> fetchShowDetails(int Id){
        final MutableLiveData<DetailedShow> show = new MutableLiveData<>();

        api.getShowDetails(Id).enqueue(new Callback<DetailedShow>() {
            @Override
            public void onResponse(Call<DetailedShow> call, Response<DetailedShow> response) {
                if (!response.isSuccessful()) { return; }

                // removing html tags from the show description before returning the show
                // adapted from https://www.w3docs.com/snippets/java/remove-html-tags-from-a-string.html
                response.body().setDescription(response.body().getDescription().replaceAll("<[^>]+>", ""));
                show.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DetailedShow> call, Throwable t) {
                Log.i("fetchShowDetails", call.request().toString());
                Log.e("fetchShowDetails", t.getMessage());
                show.setValue(null);
            }
        });
        return show;
    }

    public LiveData<Episode> fetchEpisodes(int showId, int season, int epNum){
        final MutableLiveData<Episode> episode = new MutableLiveData<>();

        api.getEpisode(showId, season, epNum).enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Call<Episode> call, Response<Episode> response) {
                episode.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Episode> call, Throwable t) {
                Log.i("fetchEpisode", call.request().toString());
                Log.e("fetchEpisode", t.getMessage());
                episode.setValue(null);
            }
        });
        return episode;
    }
}
