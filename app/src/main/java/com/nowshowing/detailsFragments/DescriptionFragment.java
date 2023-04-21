package com.nowshowing.detailsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.api.RestRepository;

import java.util.ArrayList;

public class DescriptionFragment extends Fragment {
    private TextView title;
    private TextView genres;
    private TextView status;
    private TextView rating;
    private TextView description;
    private int show_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        // get show id from parent activity
        ShowDetailsActivity parent = (ShowDetailsActivity) getActivity();
        show_id = parent.getShowId();

        title = view.findViewById(R.id.show_title);
        genres = view.findViewById(R.id.genres);
        status = view.findViewById(R.id.status_value);
        rating = view.findViewById(R.id.rating_value);
        description = view.findViewById(R.id.description);
        Button fav = view.findViewById(R.id.btn_favourite);

        // make get request and populate the layout
        fetchDetails();

        // set on click listener for favourites button
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(parent, "Please log in to add a show to favourites", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void fetchDetails(){
        RestRepository.getInstance().fetchShowDetails(show_id).observe(getViewLifecycleOwner(), this::updateDetails);
    }

    private void updateDetails(DetailedShow updated){
        populateLayout(updated); // update the UI
    }

    private void populateLayout(@NonNull DetailedShow show){
        title.setText(show.getName());

        ArrayList<String> show_genres = show.getGenres();
        String genreList = String.join("|", show_genres);
        genres.setText(genreList);

        status.setText(show.getStatus());
        rating.setText(Double.toString(show.getRating().getAverageRating()));
        description.setText(show.getDescription());
    }
}