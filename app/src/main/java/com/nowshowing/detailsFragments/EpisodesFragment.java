package com.nowshowing.detailsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.api.RestRepository;
import com.nowshowing.models.Episode;
import com.nowshowing.models.Season;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodesFragment extends Fragment {
    private int show_id;
    private ImageView image;
    private TextView season_and_ep;
    private TextView title;
    private TextView date;
    private CheckBox checkbox;
    private SeasonsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Season> seasons = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episodes, container, false);
        // get show id from parent activity
        ShowDetailsActivity parent = (ShowDetailsActivity) getActivity();
        show_id = parent.getShowId();

        image = view.findViewById(R.id.ep_card).findViewById(R.id.ep_image);
        season_and_ep = view.findViewById(R.id.season_ep_number);
        title = view.findViewById(R.id.ep_title);
        date = view.findViewById(R.id.ep_release_date);
        checkbox = view.findViewById(R.id.set_watched);

        // set up the recycler view adapter and layout manager
        recyclerView = view.findViewById(R.id.seasons_list);
        adapter = new SeasonsAdapter(seasons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        // make get request and populate the layout
        fetchEpisode();

        // get list of seasons
        fetchSeasons();

        // handle checked/unchecked checkbox
        // inspired by https://stackoverflow.com/questions/8386832/android-checkbox-listener
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // if the user is not logged in, automatically uncheck
                    Toast.makeText(parent, "Please log in to set episode as watched", Toast.LENGTH_LONG).show();
                    checkbox.setChecked(false);
                }
                else
                {
                    // unchecked
                    Toast.makeText(parent, "Episode removed from watched list", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void fetchEpisode(){
        // if the user is not logged in, the default episode is always the first episode of the first season
        RestRepository.getInstance().fetchEpisodes(show_id, 1, 1).observe(getViewLifecycleOwner(), this::updateEpisode);
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
    }

    private void fetchSeasons(){
        RestRepository.getInstance().fetchSeasons(show_id).observe(getViewLifecycleOwner(), this::updateSeasonsList);
    }

    private void updateSeasonsList(List<Season> updatedList){
        seasons.addAll(updatedList);
        adapter.notifyDataSetChanged();
    }
}