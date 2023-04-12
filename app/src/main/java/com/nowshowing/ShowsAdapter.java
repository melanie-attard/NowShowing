package com.nowshowing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
    private List<Show> shows;

    public ShowsAdapter(List<Show> shows) { this.shows = shows; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.layout_show_horizontal, parent, false);
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Show show = shows.get(position);
        holder.show_name.setText(show.getName());

        // join all genres into one string separated by |
        ArrayList<String> genres = show.getGenres();
        String genreList = String.join("|", genres);
        holder.show_genres.setText(genreList);

        // TODO download image from internet https://www.tutorialspoint.com/how-do-i-load-an-imageview-by-url-on-android
        holder.show_image.setImageResource(R.drawable.ic_profile_black);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_name;
        TextView show_genres;
        ImageView show_image;

        public ViewHolder(final View showView){
            super(showView);
            show_name = showView.findViewById(R.id.show_name);
            show_genres = showView.findViewById(R.id.genres);
            show_image = showView.findViewById(R.id.show_image);
        }
    }
}
