package com.nowshowing.detailsFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.api.RestRepository;
import com.nowshowing.backend.FavouritesDBHelper;
import com.nowshowing.models.DetailedShow;

import java.util.ArrayList;

public class DescriptionFragment extends Fragment {
    private TextView title;
    private TextView genres;
    private TextView status;
    private TextView rating;
    private TextView description;
    private int show_id;
    private String user;
    private FavouritesDBHelper DB;
    private Boolean isFav;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        // get show id from parent activity
        ShowDetailsActivity parent = (ShowDetailsActivity) getActivity();
        show_id = parent.getShowId();

        DB = new FavouritesDBHelper(view.getContext());
        // retrieve shared preferences containing the current user
        sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext().getApplicationContext());
        user = sharedPref.getString(getString(R.string.current_user_key), null);

        title = view.findViewById(R.id.show_title);
        genres = view.findViewById(R.id.genres);
        status = view.findViewById(R.id.status_value);
        rating = view.findViewById(R.id.rating_value);
        description = view.findViewById(R.id.description);
        Button fav = view.findViewById(R.id.btn_favourite);

        if(user != null){
            // set button text according to whether the show is in favourites
            isFav = DB.isFavourite(user, show_id);
            if(isFav){
                fav.setText(R.string.remove_fav_btn);
            }
            else{
                fav.setText(R.string.add_favourite_btn);
            }
        }

        // make get request and populate the layout
        fetchDetails();

        // set on click listener for favourites button
        fav.setOnClickListener(view1 -> {
            // check if the user is logged in
            String user = sharedPref.getString(getString(R.string.current_user_key), null);
            if(user == null){
                Toast.makeText(parent, "Please log in to add a show to favourites", Toast.LENGTH_LONG).show();
            }
            else{
                // check whether we are adding or removing
                isFav = DB.isFavourite(user, show_id);
                if(isFav){
                    // remove the row from the database
                    Boolean result = DB.Delete(user, show_id);
                    if(result){
                        // update button text to 'Add to Favourites'
                        fav.setText(R.string.add_favourite_btn);
                    }
                    else{
                        // an error occurred
                        Toast.makeText(parent, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    // add show to favourites
                    Boolean result = DB.insertFavourite(user, show_id);
                    if(result){
                        // update button text to 'Remove from Favourites'
                        fav.setText(R.string.remove_fav_btn);
                    }
                    else{
                        // an error occurred
                        Toast.makeText(parent, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
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