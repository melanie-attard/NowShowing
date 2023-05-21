package com.nowshowing.mainFragments.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nowshowing.R;
import com.nowshowing.backend.UserDBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    UserDBHelper DB;
    TextInputLayout username, email, password, repassword;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DB = new UserDBHelper(this);
        username = findViewById(R.id.username1);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password1);
        repassword = findViewById(R.id.confirm_password);
        register_btn = findViewById(R.id.btn_register);

        register_btn.setOnClickListener(view -> {
            String user = username.getEditText().getText().toString();
            String pass = password.getEditText().getText().toString();
            String Email = email.getEditText().getText().toString();
            String confirmpass = repassword.getEditText().getText().toString();

            // reset all error fields
            username.setError(null);
            password.setError(null);
            email.setError(null);
            repassword.setError(null);
            boolean error = false;

            if(user.isEmpty() || pass.isEmpty() || Email.isEmpty() || confirmpass.isEmpty()){
                // switch on error flag for empty fields
                if (user.isEmpty()) {
                    username.setError("This field is required");
                    error = true;
                } else {
                    username.setError(null);
                }

                if (pass.isEmpty()) {
                    password.setError("This field is required");
                    error = true;
                } else {
                    password.setError(null);
                }

                if (Email.isEmpty()) {
                    email.setError("This field is required");
                    error = true;
                } else {
                    email.setError(null);
                }

                if (confirmpass.isEmpty()) {
                    repassword.setError("This field is required");
                    error = true;
                } else {
                    repassword.setError(null);
                }
            }

            if(!error){
                // check if email has a valid form, inspired by https://www.javatpoint.com/java-email-validation
                Pattern pemail = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
                Matcher m = pemail.matcher(Email);
                if(!m.matches()){ email.setError("Email is not in a valid format"); error = true; }

                // password must have at least six letters
                if(pass.length() < 6){ password.setError("Password must have at least six letters"); error = true; }

                // check that password is equal to the confirmed password
                if(!pass.equals(confirmpass)) {
                    password.setError("Passwords don't match");
                    repassword.setError("Passwords don't match");
                    error = true;
                }

                // check whether username already exists
                if(DB.checkUsername(user)){
                    username.setError("This username already exists");
                    error = true;
                }
                else if(user.equals(" ")){
                    username.setError("Username cannot be empty");
                    error = true;
                }

                // check that all inputs are of the required length
                if(user.length() > username.getCounterMaxLength()){
                    username.setError("Username cannot be longer than " + username.getCounterMaxLength() + " characters");
                    error = true;
                }

                if(pass.length() > password.getCounterMaxLength()){
                    password.setError("Password cannot be longer than " + password.getCounterMaxLength() + " characters");
                    error = true;
                }

                if(Email.length() > email.getCounterMaxLength()){
                    email.setError("Email cannot be longer than " + email.getCounterMaxLength() + " characters");
                    error = true;
                }

                if(confirmpass.length() > repassword.getCounterMaxLength()){
                    repassword.setError("Password cannot be longer than " + repassword.getCounterMaxLength() + " characters");
                    error = true;
                }
            }

            // if no errors are present, enter the user into the database
            if(!error){
                Boolean insertion = DB.insertUser(user, Email, pass);
                if(insertion) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    // navigate user back to account fragment
                    onBackPressed();
                }
            }
        });
    }

    public void on_backBtnClicked(View view) {
        onBackPressed();
    }
}