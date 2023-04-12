package com.nowshowing.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nowshowing.R;
import com.nowshowing.Show;
import com.nowshowing.ShowsAdapter;
import com.nowshowing.api.RestRepository;
import com.nowshowing.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ShowsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Show> shows = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.home_list);
        adapter = new ShowsAdapter(shows);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        fetchShows();
        return root;
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