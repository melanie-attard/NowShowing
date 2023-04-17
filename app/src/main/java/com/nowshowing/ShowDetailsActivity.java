package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        // get Id from intent
        Intent intent = getIntent();
        int Id = intent.getIntExtra("Id", 0);

        TextView txt = (TextView) findViewById(R.id.txt_hello);
        txt.setText("Selected: " + Id);
    }
}