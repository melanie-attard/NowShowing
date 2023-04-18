package com.nowshowing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.nowshowing.detailsFragments.VPAdapter;
import com.squareup.picasso.Picasso;

public class ShowDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private VPAdapter adapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        // set up tab layout
        // adapted from https://www.youtube.com/watch?v=EukutTtf9tQ
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewpager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new VPAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // change fragments on slide
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // get intent
        intent = getIntent();

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

    public int getShowId(){
        // allows the fragments to access the show's id
        return intent.getIntExtra("Id", 0);
    }
}