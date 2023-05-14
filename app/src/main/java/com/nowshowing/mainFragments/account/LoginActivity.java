package com.nowshowing.mainFragments.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nowshowing.R;

public class LoginActivity extends AppCompatActivity {
    Button register_now_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_now_btn = findViewById(R.id.register_now_btn);
        register_now_btn.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void on_backBtnClicked(View view) {
        onBackPressed();
    }
}