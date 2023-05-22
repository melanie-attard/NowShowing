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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nowshowing.R;
import com.nowshowing.ShowsAdapter;
import com.nowshowing.api.RestRepository;
import com.nowshowing.backend.FavouritesDBHelper;
import com.nowshowing.databinding.FragmentFavouritesBinding;
import com.nowshowing.models.Show;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesDBHelper DB;
    private RecyclerView recyclerView;
    private ShowsAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private List<Show> shows = new ArrayList<>();
    private TextView msg;
    SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DB = new FavouritesDBHelper(root.getContext());
        msg = root.findViewById(R.id.no_login_msg);
        refreshLayout = root.findViewById(R.id.refreshLayout);

        // retrieve shared preferences containing the current user
        sharedPref = PreferenceManager.getDefaultSharedPreferences(root.getContext().getApplicationContext());
        String user = sharedPref.getString(getString(R.string.current_user_key), null);
        if(user == null){
            // if the user is not logged in, they cannot view their favourites
            msg.setVisibility(View.VISIBLE);
            msg.setText(R.string.fav_not_logged_in);
        }
        else{
            msg.setVisibility(View.INVISIBLE);
            // set up recycler view
            recyclerView = root.findViewById(R.id.favourites_list);
            adapter = new ShowsAdapter(shows);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

            // SetOnRefreshListener for SwipeRefreshLayout
            refreshLayout.setOnRefreshListener(() -> {
                refreshLayout.setRefreshing(false);
                shows.clear();

                // check whether user is still logged in
                String user2 = sharedPref.getString(getString(R.string.current_user_key), null);
                if(user2 == null){
                    msg.setVisibility(View.VISIBLE);
                    msg.setText(R.string.fav_not_logged_in);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                else{
                    fetchFavourites(user2);
                }
            });

            // get data
            fetchFavourites(user);
        }
        return root;
    }

    private void fetchFavourites(String username){
        List<Integer> favs = DB.getFavourites(username);
        if(favs.size() == 0){
            // if no shows have been set to favourites yet, output a message
            msg.setVisibility(View.VISIBLE);
            msg.setText(R.string.no_favs_message);
        }
        else{
            msg.setVisibility(View.INVISIBLE);
            for (int show_id: favs) {
                RestRepository.getInstance().getShowByID(show_id).observe(getViewLifecycleOwner(), this::updateShowsList);
            }
        }
    }

    private void updateShowsList(Show show){
        shows.add(show);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}