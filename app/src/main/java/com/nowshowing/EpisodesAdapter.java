package com.nowshowing;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.nowshowing.backend.FavouritesDBHelper;
import com.nowshowing.backend.WatchedDBHelper;
import com.nowshowing.models.Episode;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>{
    private List<Episode> episodes;
    private int show_id;
    private WatchedDBHelper watchedDB;
    private FavouritesDBHelper favDB;
    private String user;

    public EpisodesAdapter(String user, List<Episode> episodes, int show_id){
        this.episodes = episodes;
        this.show_id = show_id;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View epView = inflater.inflate(R.layout.layout_episode, parent, false);
        watchedDB = new WatchedDBHelper(parent.getContext());
        favDB = new FavouritesDBHelper(parent.getContext());
        return new ViewHolder(epView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data to the layout
        Episode episode = episodes.get(position);

        if(episode.getImage() == null){
            // if there is no image for a show, default to the launcher icon
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Picasso.get()
                    .load(episode.getImage().getImgUrl())
                    .resize(300, 250)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.image);
        }

        // join season and episode number into a single string
        int s = episode.getSeason();
        int e = episode.getEpNum();
        String seasonAndEp = "S" + s + " EP" + e;
        holder.season_and_ep.setText(seasonAndEp);

        holder.title.setText(episode.getTitle());

        // format the date
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        holder.date.setText(ft.format(episode.getDate()));

        // if the episode is already set as watched, set the checkbox state to CHECKED
        if(user != null && watchedDB.isWatched(user, episode.getEpId())){
            holder.checkbox.setCheckedState(MaterialCheckBox.STATE_CHECKED);
        }

        // set on click listener for checkbox
        holder.checkbox.addOnCheckedStateChangedListener((checkBox, state) -> {
            if(user == null){
                // user is not logged in and cannot add an episode to their watched list
                Toast.makeText(holder.checkbox.getContext(),"Please log in to set an episode as watched", Toast.LENGTH_LONG).show();
                holder.checkbox.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
            }
            else {
                // check which state the checkbox is in
                if(state == MaterialCheckBox.STATE_CHECKED){
                    // if show is not in favourites, automatically add it
                    if(!favDB.isFavourite(user, show_id)){
                        Boolean result = favDB.insertFavourite(user, show_id);
                        if(result){
                            Toast.makeText(holder.checkbox.getContext(), "This show is now in your Favourites", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // add episode to watchlist
                    Boolean result = watchedDB.insertEpisode(user, show_id, episode.getEpId());
                    if(!result){
                        Toast.makeText(holder.checkbox.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
                        holder.checkbox.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
                    }
                }
                else{
                    // remove episode from watchlist
                    Boolean result = watchedDB.remove(user, episode.getEpId());
                    if(!result){
                        Toast.makeText(holder.checkbox.getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
                        holder.checkbox.setCheckedState(MaterialCheckBox.STATE_CHECKED);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView season_and_ep;
        TextView title;
        TextView date;
        MaterialCheckBox checkbox;

        public ViewHolder(final View epView) {
            super(epView);
            image = epView.findViewById(R.id.ep_image);
            season_and_ep = epView.findViewById(R.id.season_ep_number);
            title = epView.findViewById(R.id.ep_title);
            date = epView.findViewById(R.id.ep_release_date);
            checkbox = epView.findViewById(R.id.set_watched);
        }
    }
}
