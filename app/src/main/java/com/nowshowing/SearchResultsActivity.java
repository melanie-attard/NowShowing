package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SearchResultsActivity extends AppCompatActivity {
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        TextView query = findViewById(R.id.textView);
        query.setText(intent.getStringExtra("query"));
    }
}