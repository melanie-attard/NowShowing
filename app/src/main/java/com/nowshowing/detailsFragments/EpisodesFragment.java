package com.nowshowing.detailsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.api.RestRepository;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class EpisodesFragment extends Fragment {
    private int show_id;
    ImageView image;
    TextView season_and_ep;
    TextView title;
    TextView date;
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

        // make get request and populate the layout
        fetchEpisode();

        //TODO handle checked checkbox
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
}