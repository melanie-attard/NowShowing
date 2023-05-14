package com.nowshowing.mainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nowshowing.R;
import com.nowshowing.databinding.FragmentAccountBinding;
import com.nowshowing.mainFragments.account.LoginActivity;
import com.nowshowing.mainFragments.account.RegisterActivity;

public class AccountFragment extends Fragment {

    Button login;
    Button register;
    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        login = root.findViewById(R.id.login_btn);
        register = root.findViewById(R.id.register_btn);

        // set an on-click listener for each button
        login.setOnClickListener(view -> {
            Intent intent = new Intent(root.getContext(), LoginActivity.class);
            startActivity(intent);
        });

        register.setOnClickListener(view -> {
            Intent intent = new Intent(root.getContext(), RegisterActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}