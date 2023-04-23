package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nowshowing.api.RestRepository;
import com.nowshowing.models.Show;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
    TextView header;
    private String query;
    List<Show> results = new ArrayList<>();
    RecyclerView recyclerView;
    ShowsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_generic_activity);

        // set header to 'Results for'
        header = findViewById(R.id.title1);
        header.setText(R.string.search_result_header);

        // get query from intent
        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        TextView title2 = findViewById(R.id.title2);
        title2.setText(intent.getStringExtra("query"));

        // set up recycler
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ShowsAdapter(results);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        Search(query);
    }

    private void Search(String query){
        RestRepository.getInstance().getSearchResults(query).observe(this, this::updateResults);
    }

    private void updateResults(List<Show> newResults){
        results.addAll(newResults);
        adapter.notifyDataSetChanged();
    }

    public void on_backBtnClicked(View v){
        onBackPressed();
    }
}