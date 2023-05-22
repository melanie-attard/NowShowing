package com.nowshowing.mainFragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nowshowing.R;
import com.nowshowing.models.Show;
import com.nowshowing.ShowsAdapter;
import com.nowshowing.api.RestRepository;
import com.nowshowing.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ShowsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView no_internet;
    private List<Show> shows = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // this will call onPrepareOptionsMenu
        setHasOptionsMenu(true);

        // check for internet connectivity,
        // inspired by https://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android#:~:text=This%20will%20tell%20if%20you%27re%20connected%20to%20a%20network
        ConnectivityManager connectivityManager = (ConnectivityManager)root.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(connected){
            // populate recycler view
            recyclerView = root.findViewById(R.id.home_list);
            adapter = new ShowsAdapter(shows);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

            fetchShows();
        }
        else {
            // show error message
            //recyclerView.setVisibility(View.INVISIBLE);
            no_internet = root.findViewById(R.id.network_error);
            no_internet.setVisibility(View.VISIBLE);
        }

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // setting the search view item as visible,
        // retrieved from https://stackoverflow.com/questions/8308695/how-to-add-options-menu-to-fragment-in-android#:~:text=Add%20setHasOptionsMenu(true)%20in%20the,items%20in%20your%20Fragment%20class.&text=You%20don't%20need%20to,onPrepareOptionsMenu%20method%20available%20in%20Fragment.
        menu.findItem(R.id.search_bar).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    private void fetchShows(){
        RestRepository.getInstance().fetchShows().observe(getViewLifecycleOwner(), this::updateList);
    }

    private void updateList(List<Show> newItems){
        shows.addAll(newItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}