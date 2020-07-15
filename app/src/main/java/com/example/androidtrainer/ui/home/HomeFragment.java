package com.example.androidtrainer.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtrainer.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.doist.recyclerviewext.sticky_headers.StickyHeadersLinearLayoutManager;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.list_home);
        final StickyHeadersLinearLayoutManager<HomeAdapter> layoutManager =
                new StickyHeadersLinearLayoutManager<>(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        final HomeAdapter adapter = new HomeAdapter(container.getContext());
        recyclerView.setAdapter(adapter);

        Context context = getContext();
        if (context != null) {
            try {
                InputStream inputStream = getContext().getAssets().open("questions");
                homeViewModel.getText(inputStream)
                        .observe(getViewLifecycleOwner(), new Observer<List<HomeViewModel.Question>>() {
                            @Override
                            public void onChanged(@Nullable final List<HomeViewModel.Question> questions) {
                                adapter.setItems(questions);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return root;
    }
}