package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        // get image from intent
        ImageView imageView = findViewById(R.id.imageView);
        String image = String.valueOf(intent.getStringExtra("Image"));
        Picasso.get()
                .load(image)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public void on_backBtnClicked(View view) {
        onBackPressed();
    }
}