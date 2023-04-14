package com.nowshowing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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

        // load image from URL
        Picasso.get()
                .load(show.getImages().getImg())
                .error(R.mipmap.ic_launcher)
                .resize(400,400)
                .centerCrop()
                .into(holder.show_image);
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

            // set on-click listener for every list item
            showView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), ShowDetailsActivity.class);
//                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
