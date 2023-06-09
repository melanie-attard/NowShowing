package com.nowshowing.detailsFragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowshowing.EpisodesListActivity;
import com.nowshowing.R;
import com.nowshowing.ShowDetailsActivity;
import com.nowshowing.ShowsAdapter;
import com.nowshowing.models.Season;

import java.util.List;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.ViewHolder>{
    private List<Season> seasons;
    private int show_id;
    public SeasonsAdapter(List<Season> seasons, int show_id){
        this.seasons = seasons;
        this.show_id = show_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View seasonView = inflater.inflate(R.layout.layout_season_item, parent, false);
        return new ViewHolder(seasonView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data to the layout of each season
        Season s = seasons.get(position);
        holder.season_num.setText(Integer.toString(s.getNum()));
        holder.ep_count.setText(Integer.toString(s.getEpCount()));
    }

    @Override
    public int getItemCount() { return seasons.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView season_num;
        TextView ep_count;

        public ViewHolder(final View seasonView){
            super(seasonView);
            season_num = seasonView.findViewById(R.id.season_num);
            ep_count = seasonView.findViewById(R.id.ep_count);

            // setting an on-click listener for every list item
            seasonView.setOnClickListener(view -> {
                int pos = getAdapterPosition(); // get position of item
                int id = seasons.get(pos).getSeasonId();

                Intent intent = new Intent(view.getContext(), EpisodesListActivity.class);
                intent.putExtra("Id", id);
                intent.putExtra("SeasonNum", season_num.getText());
                intent.putExtra("Show_Id", show_id);
                view.getContext().startActivity(intent);
            });
        }
    }
}
