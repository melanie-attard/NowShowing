package com.nowshowing.mainFragments.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nowshowing.R;
import com.nowshowing.backend.UserDBHelper;

public class LoginActivity extends AppCompatActivity {
    private UserDBHelper DB;
    private TextInputLayout username, password;
    private Button login_btn, register_now_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DB = new UserDBHelper(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.btn_login);
        register_now_btn = findViewById(R.id.register_now_btn);

        // set on click listeners for both buttons
        login_btn.setOnClickListener(view -> {
            String user = username.getEditText().getText().toString();
            String pass = password.getEditText().getText().toString();

            // check that the user inputted the correct credentials
            if(user.isEmpty() && pass.isEmpty()){
                username.setError("This field is required");
                password.setError("This field is required");
            }
            else if(user.isEmpty()){
                username.setError("This field is required");
                password.setError(null);
            }
            else if(pass.isEmpty()){
                username.setError(null);
                password.setError("This field is required");
            }
            else{
                // reset error messages
                username.setError(null);
                password.setError(null);

                Boolean exists = DB.userExists(user, pass);
                if(exists){
                    Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                    // navigate user back to account fragment
                    onBackPressed();
                }
                else{
                    Toast.makeText(this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register_now_btn.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void on_backBtnClicked(View view) {
        onBackPressed();
    }
}