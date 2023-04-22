package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nowshowing.api.RestRepository;
import com.nowshowing.models.Episode;
import com.nowshowing.models.Show;

import java.util.ArrayList;
import java.util.List;

public class EpisodesListActivity extends AppCompatActivity {
    private Intent intent;
    private List<Episode> episodes = new ArrayList<>();
    TextView season_num;
    RecyclerView recyclerView;
    EpisodesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_list);

        // get values in intent
        intent = getIntent();
        int seasonId = intent.getIntExtra("Id", 0);
        season_num = findViewById(R.id.snum);
        season_num.setText(intent.getStringExtra("SeasonNum"));

        recyclerView = findViewById(R.id.ep_list);
        adapter = new EpisodesAdapter(episodes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        fetchEpisodes(seasonId);
    }

    private void fetchEpisodes(int seasonId){
        RestRepository.getInstance().getEpisodesBySeason(seasonId).observe(this, this::updateList);
    }

    private void updateList(List<Episode> newItems){
        episodes.addAll(newItems);
        adapter.notifyDataSetChanged();
    }

    public void on_backBtnClicked(View view) {
        onBackPressed();
    }
}