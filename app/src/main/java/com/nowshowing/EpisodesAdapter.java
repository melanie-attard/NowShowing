package com.nowshowing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowshowing.models.Episode;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>{
    private List<Episode> episodes;

    public EpisodesAdapter(List<Episode> episodes){ this.episodes = episodes; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View epView = inflater.inflate(R.layout.layout_episode, parent, false);
        return new ViewHolder(epView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data to the layout
        Episode episode = episodes.get(position);
        Picasso.get()
                .load(episode.getImage().getImgUrl())
                .resize(300, 250)
                .error(R.mipmap.ic_launcher)
                .into(holder.image);

        // join season and episode number into a single string
        int s = episode.getSeason();
        int e = episode.getEpNum();
        String seasonAndEp = "S" + s + " EP" + e;
        holder.season_and_ep.setText(seasonAndEp);

        holder.title.setText(episode.getTitle());

        // format the date
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        holder.date.setText(ft.format(episode.getDate()));
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
        CheckBox checkbox;

        public ViewHolder(final View epView) {
            super(epView);
            image = epView.findViewById(R.id.ep_image);
            season_and_ep = epView.findViewById(R.id.season_ep_number);
            title = epView.findViewById(R.id.ep_title);
            date = epView.findViewById(R.id.ep_release_date);
            checkbox = epView.findViewById(R.id.set_watched);

            //TODO set on click listener for checkbox
        }
    }
}
