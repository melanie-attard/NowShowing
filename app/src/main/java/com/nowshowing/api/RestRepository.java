package com.nowshowing.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nowshowing.models.SearchResult;
import com.nowshowing.models.Season;
import com.nowshowing.models.Show;
import com.nowshowing.models.DetailedShow;
import com.nowshowing.models.Episode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

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
        final MutableLiveData<List<Show>> shows = new MutableLiveData<>();

        api.getShows().enqueue(new Callback<List<Show>>() {
            @Override
            public void onResponse(@NonNull Call<List<Show>> call, @NonNull Response<List<Show>> response) {
                if (!response.isSuccessful()) { return; }
                shows.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Show>> call, @NonNull Throwable t) {
                Log.i("fetchShows", call.request().toString());
                Log.e("fetchShows", t.getMessage());
                shows.setValue(null);
            }
        });
        return shows;
    }

    public LiveData<Show> getShowByID(int Id){
        final MutableLiveData<Show> show = new MutableLiveData<>();

        api.getShowByID(Id).enqueue(new Callback<Show>() {
            @Override
            public void onResponse(@NonNull Call<Show> call, @NonNull Response<Show> response) {
                if (!response.isSuccessful()) { return; }
                show.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Show> call, @NonNull Throwable t) {
                Log.i("fetchShow", call.request().toString());
                Log.e("fetchShow", t.getMessage());
                show.setValue(null);
            }
        });
        return show;
    }

    public LiveData<DetailedShow> fetchShowDetails(int Id){
        final MutableLiveData<DetailedShow> show = new MutableLiveData<>();

        api.getShowDetails(Id).enqueue(new Callback<DetailedShow>() {
            @Override
            public void onResponse(@NonNull Call<DetailedShow> call, @NonNull Response<DetailedShow> response) {
                if (!response.isSuccessful()) { return; }

                // removing html tags from the show description before returning the show
                // adapted from https://www.w3docs.com/snippets/java/remove-html-tags-from-a-string.html
                response.body().setDescription(response.body().getDescription().replaceAll("<[^>]+>", ""));
                show.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DetailedShow> call, @NonNull Throwable t) {
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
            public void onResponse(@NonNull Call<Episode> call, @NonNull Response<Episode> response) {
                if (!response.isSuccessful()) { return; }
                episode.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Episode> call, @NonNull Throwable t) {
                Log.i("fetchEpisode", call.request().toString());
                Log.e("fetchEpisode", t.getMessage());
                episode.setValue(null);
            }
        });
        return episode;
    }

    public LiveData<List<Season>> fetchSeasons(int Id){
        final MutableLiveData<List<Season>> seasons = new MutableLiveData<>();

        api.getSeasons(Id).enqueue(new Callback<List<Season>>() {
            @Override
            public void onResponse(@NonNull Call<List<Season>> call, @NonNull Response<List<Season>> response) {
                if (!response.isSuccessful()) { return; }
                seasons.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Season>> call, @NonNull Throwable t) {
                Log.i("fetchSeasons", call.request().toString());
                Log.e("fetchSeasons", t.getMessage());
                seasons.setValue(null);
            }
        });
        return seasons;
    }

    public LiveData<List<Episode>> getEpisodesBySeason(int Id){
        final MutableLiveData<List<Episode>> episodes = new MutableLiveData<>();

        api.getEpisodesBySeason(Id).enqueue(new Callback<List<Episode>>() {
            @Override
            public void onResponse(@NonNull Call<List<Episode>> call, @NonNull Response<List<Episode>> response) {
                if (!response.isSuccessful()) { return; }
                episodes.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Episode>> call, @NonNull Throwable t) {
                Log.i("fetchEpisodeList", call.request().toString());
                Log.e("fetchEpisodeList", t.getMessage());
                episodes.setValue(null);
            }
        });
        return episodes;
    }

    public LiveData<List<Show>> getSearchResults(String query){
        final MutableLiveData<List<Show>> results = new MutableLiveData<>();

        api.getSearchResults(query).enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchResult>> call, @NonNull Response<List<SearchResult>> response) {
                if (!response.isSuccessful()) { return; }
                // convert into List<Show>
                List<Show> list = new ArrayList<>();
                for (SearchResult result: response.body()) {
                    list.add(result.getShow());
                }
                results.setValue(list);
            }

            @Override
            public void onFailure(@NonNull Call<List<SearchResult>> call, @NonNull Throwable t) {
                Log.i("fetchSearchResults", call.request().toString());
                Log.e("fetchSearchResults", t.getMessage());
                results.setValue(null);
            }
        });
        return results;
    }
}