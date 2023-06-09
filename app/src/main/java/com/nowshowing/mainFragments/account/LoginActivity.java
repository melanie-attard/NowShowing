package com.nowshowing.mainFragments.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get the shared preference file to update the logged in user,
        // inspired by https://stackoverflow.com/questions/11316560/sharedpreferences-from-different-activity
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
            else if(user.length() > username.getCounterMaxLength() && pass.length() > password.getCounterMaxLength()){
                // check that all inputs are of the required length
                username.setError("Username cannot be longer than " + username.getCounterMaxLength() + " characters");
                password.setError("Password cannot be longer than " + password.getCounterMaxLength() + " characters");
            }
            else if(pass.length() > password.getCounterMaxLength()){
                password.setError("Password cannot be longer than " + password.getCounterMaxLength() + " characters");
                username.setError(null);
            }
            else if(user.length() > username.getCounterMaxLength()){
                username.setError("Username cannot be longer than " + username.getCounterMaxLength() + " characters");
                password.setError(null);
            }
            else{
                // reset error messages
                username.setError(null);
                password.setError(null);

                Boolean exists = DB.userExists(user, pass);
                if(exists){
                    // set the user as current user in shared preferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.current_user_key), user);
                    editor.apply();

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