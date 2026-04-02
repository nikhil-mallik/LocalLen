package com.example.locallens.ui.ui.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locallens.R;
import com.example.locallens.domain.model.Place;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<Place> places = new ArrayList<>();

    public void setData(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_place, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Place place = places.get(position);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return places != null ? places.size() : 0;
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPlaceImage;
        private final TextView tvPlaceName;
        private final TextView tvCategory;
        private final TextView tvRating;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaceImage = itemView.findViewById(R.id.ivPlaceImage);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvRating = itemView.findViewById(R.id.tvRating);
        }

        public void bind(Place place) {
            tvPlaceName.setText(place.getName());
            tvCategory.setText(place.getCategory());
            tvRating.setText(String.format("⭐ %s", place.getRating()));

            Glide.with(itemView.getContext())
                    .load(place.getImageUrl())
                    .centerCrop()
                    .into(ivPlaceImage);
        }
    }
}
