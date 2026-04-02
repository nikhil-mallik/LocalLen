package com.example.locallens.ui.ui.favorites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locallens.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoritesActivity extends AppCompatActivity {

    private FavoritesViewModel viewModel;
    private FavoritesAdapter adapter;

    private RecyclerView recyclerFavorites;
    private TextView tvEmptyFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerFavorites = findViewById(R.id.recyclerFavorites);
        tvEmptyFavorites = findViewById(R.id.tvEmptyFavorites);

        adapter = new FavoritesAdapter();
        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavorites.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        viewModel.getFavorites().observe(this, places -> {
            if (places == null || places.isEmpty()) {
                tvEmptyFavorites.setVisibility(View.VISIBLE);
                recyclerFavorites.setVisibility(View.GONE);
            } else {
                tvEmptyFavorites.setVisibility(View.GONE);
                recyclerFavorites.setVisibility(View.VISIBLE);
                adapter.setData(places);
            }
        });

        viewModel.loadFavorites();
    }
}
