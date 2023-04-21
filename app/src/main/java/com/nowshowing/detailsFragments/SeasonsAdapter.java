package com.nowshowing.detailsFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nowshowing.R;
import com.nowshowing.ShowsAdapter;
import com.nowshowing.models.Season;

import java.util.List;

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.ViewHolder>{
    private List<Season> seasons;
    public SeasonsAdapter(List<Season> seasons){ this.seasons = seasons; }

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
        // populate the layout of each season
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
            seasonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
