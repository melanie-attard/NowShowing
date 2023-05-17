package com.nowshowing.mainFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nowshowing.R;
import com.nowshowing.backend.FavouritesDBHelper;
import com.nowshowing.databinding.FragmentFavouritesBinding;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesDBHelper DB;
    private List<Integer> favs = new ArrayList<>();
    SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DB = new FavouritesDBHelper(root.getContext());
        TextView msg = root.findViewById(R.id.no_login_msg);

        // retrieve shared preferences containing the current user
        sharedPref = PreferenceManager.getDefaultSharedPreferences(root.getContext().getApplicationContext());
        String user = sharedPref.getString(getString(R.string.current_user_key), null);
        if(user == null){
            // if the user is not logged in, they cannot view their favourites
            msg.setVisibility(View.VISIBLE);
        }
        else{
            favs = DB.getFavourites(user);
            msg.setVisibility(View.VISIBLE);
            msg.setText("You have " + favs.size() + " favourites.");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}