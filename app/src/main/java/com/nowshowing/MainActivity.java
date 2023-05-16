package com.nowshowing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.nowshowing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    SharedPreferences sharedPref;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // get text view in drawer header
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.current_username);

        // retrieve shared preferences containing the current user
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // refreshing the drawer layout everytime it is opened,
        // adapted from https://itecnote.com/tecnote/android-refresh-header-in-navigation-drawer/
        ActionBarDrawerToggle DrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.title_activity_main);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(null);

                // get logged in user (default value is null)
                user = sharedPref.getString(getString(R.string.current_user_key), null);
                if(user != null) {
                    // set drawer header username, if a user is logged in
                    username.setText(user);
                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawer.addDrawerListener(DrawerToggle);
        DrawerToggle.setDrawerIndicatorEnabled(true);

        // setup of log out button
        Button logout = navigationView.findViewById(R.id.btn_Logout);
        logout.setOnClickListener(view -> {
            // get logged in user (default value is null)
            user = sharedPref.getString(getString(R.string.current_user_key), null);
            if(user != null){
                // set current user to null
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.current_user_key), null);
                editor.apply();

                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                // remove username from header
                username.setText(null);
            }
        } );

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_fav, R.id.nav_account)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // setting up the search view, this is only visible in the home fragment
        // inspired by https://www.geeksforgeeks.org/how-to-implement-android-searchview-with-example/
        MenuItem searchView = menu.findItem(R.id.search_bar);
        SearchView search = (SearchView) MenuItemCompat.getActionView(searchView);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(search.getContext(), SearchResultsActivity.class);
                intent.putExtra("query", query);
                search.getContext().startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}