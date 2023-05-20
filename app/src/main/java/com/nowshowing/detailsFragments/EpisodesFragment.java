package com.nowshowing.detailsFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.api.RestRepository;
import com.nowshowing.backend.FavouritesDBHelper;
import com.nowshowing.backend.WatchedDBHelper;
import com.nowshowing.models.Episode;
import com.nowshowing.models.Season;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesFragment extends Fragment {
    private int show_id;
    private boolean refreshing;
    private ImageView image, show_completed;
    private TextView season_and_ep;
    private TextView title;
    private TextView date;
    private MaterialCheckBox checkbox;
    private LinearProgressIndicator progressIndicator;
    private Button reset_btn;
    private RelativeLayout episodeCard;
    private SeasonsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Season> seasons = new ArrayList<>();
    private String user;
    private int watched;
    private FavouritesDBHelper favDB;
    private WatchedDBHelper watchedDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episodes, container, false);
        // get show id from parent activity
        ShowDetailsActivity parent = (ShowDetailsActivity) getActivity();
        show_id = parent.getShowId();

        // retrieve shared preferences containing the current user
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
        user = prefs.getString(getString(R.string.current_user_key), null);

        favDB = new FavouritesDBHelper(view.getContext());
        watchedDB = new WatchedDBHelper(view.getContext());
        refreshing = false;

        image = view.findViewById(R.id.ep_card).findViewById(R.id.ep_image);
        show_completed = view.findViewById(R.id.show_completed);
        season_and_ep = view.findViewById(R.id.season_ep_number);
        title = view.findViewById(R.id.ep_title);
        date = view.findViewById(R.id.ep_release_date);
        progressIndicator = view.findViewById(R.id.progress_indicator);
        checkbox = view.findViewById(R.id.set_watched);
        reset_btn = view.findViewById(R.id.reset_show_btn);
        episodeCard = view.findViewById(R.id.card_container);

        // set up the recycler view adapter and layout manager
        recyclerView = view.findViewById(R.id.seasons_list);
        adapter = new SeasonsAdapter(seasons, show_id);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        // get list of seasons
        fetchSeasons();

        // defining on-click listener to reset progress
        reset_btn.setOnClickListener(view1 -> {
            if(user != null){
                // delete all episodes from the watched table
                watchedDB.resetShow(user, show_id);
                // reset UI
                refreshing = true;
                fetchSeasons();
            }
            else {
                Toast.makeText(view.getContext(), "Please log in to access this feature", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void fetchSeasons(){
        RestRepository.getInstance().fetchSeasons(show_id).observe(getViewLifecycleOwner(), this::updateSeasonsList);
    }

    private void updateSeasonsList(List<Season> updatedList){
        if(!refreshing){
            seasons.addAll(updatedList);
        }

        // calling fetchEpisode from here to have access to seasons list
        fetchEpisode();
        adapter.notifyDataSetChanged();
    }

    private void fetchEpisode(){
        // if the user is not logged in, the default episode is always the first episode of the first season
        if(user == null){
            RestRepository.getInstance().fetchEpisodes(show_id, 1, 1).observe(getViewLifecycleOwner(), this::updateEpisode);
        }
        else{
            watched = watchedDB.getWatchedCount(user, show_id);
            if(watched == 0){
                // get the first episode of the first season
                RestRepository.getInstance().fetchEpisodes(show_id, 1, 1).observe(getViewLifecycleOwner(), this::updateEpisode);

                // ensure the card is visible
                episodeCard.setVisibility(View.VISIBLE);
                show_completed.setVisibility(View.INVISIBLE);
            }
            else{
                // calculate which episode is next
                boolean found = false;
                for (Season s: seasons) {
                    if(watched >= s.getEpCount()){
                        // subtract the number of episodes in this season and move to the next
                        watched -= s.getEpCount();
                    }
                    else{
                        found = true;
                        RestRepository.getInstance().fetchEpisodes(show_id, s.getNum(), watched+1).observe(getViewLifecycleOwner(), this::updateEpisode);
                        break;
                    }
                }

                if(!found){
                    // the user has watched all the available episodes,
                    // hide the episode card and display an icon
                    episodeCard.setVisibility(View.INVISIBLE);
                    show_completed.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void updateEpisode(Episode updated){
        populateLayout(updated); // update the UI
    }

    private void populateLayout(@NonNull Episode episode) {
        Picasso.get()
                .load(episode.getImage().getImgUrl())
                .resize(300, 250)
                .error(R.mipmap.ic_launcher)
                .into(image);

        // join season and episode number into a single string
        int s = episode.getSeason();
        int e = episode.getEpNum();
        String seasonAndEp = "S" + s + " EP" + e;
        season_and_ep.setText(seasonAndEp);

        title.setText(episode.getTitle());

        // format the date
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(ft.format(episode.getDate()));

        // calculate progress for progress bar
        int totalEps = 0;
        for (Season season: seasons) {
            totalEps += season.getEpCount();
        }
        // set progress percentage
        progressIndicator.setProgress((100*watched)/totalEps);

        // handle checked/unchecked checkbox
        checkbox.addOnCheckedStateChangedListener((checkBox, state) -> {
            if(user == null){
                // user is not logged in and cannot add an episode to their watched list
                Toast.makeText(checkbox.getContext(),"Please log in to set an episode as watched", Toast.LENGTH_LONG).show();
                checkbox.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
            }
            else {
                // in this case the checkbox can only be 'checked' since the shown episode is the next to watch
                if(state == MaterialCheckBox.STATE_CHECKED){
                    // if show is not in favourites, automatically add it
                    if(!favDB.isFavourite(user, show_id)){
                        Boolean result = favDB.insertFavourite(user, show_id);
                        if(result){
                            Toast.makeText(checkbox.getContext(), "This show is now in your Favourites", Toast.LENGTH_LONG).show();
                        }
                    }

                    // add episode to watchlist
                    Boolean result = watchedDB.insertEpisode(user, show_id, episode.getEpId());
                    if(!result){
                        Toast.makeText(checkbox.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
                        checkbox.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
                    }
                    else{
                        // update UI with next episode
                        refreshing = true;
                        fetchSeasons();
                        checkbox.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
                    }
                }
            }
        });
    }
}