package com.example.locallens.ui.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.locallens.R;
import com.example.locallens.domain.model.Place;

import java.util.List;

public class SwipeCardAdapter extends BaseAdapter {

    public interface CardSwipeListener {
        void onSwipeLeft(Place place);
        void onSwipeRight(Place place);
    }

    private final Context context;
    private final List<Place> places;
    private final CardSwipeListener listener;

    public SwipeCardAdapter(Context context, List<Place> places, CardSwipeListener listener) {
        this.context = context;
        this.places = places;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return places != null ? places.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_place_card, parent, false);
        }

        Place place = places.get(position);

        ImageView ivPlaceImage = convertView.findViewById(R.id.ivPlaceImage);
        TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvRating = convertView.findViewById(R.id.tvRating);

        tvPlaceName.setText(place.getName());
        tvCategory.setText(place.getCategory());
        tvRating.setText(String.format("⭐ %s", place.getRating()));

        Glide.with(context)
                .load(place.getImageUrl())
                .centerCrop()
                .into(ivPlaceImage);

        // Attach native Gesture Detector
        GestureDetector gestureDetector = new GestureDetector(context, new SwipeGestureListener(new SwipeGestureListener.OnSwipeListener() {
            @Override
            public void onSwipeLeft() {
                if (places.contains(place)) {
                    places.remove(place);
                    notifyDataSetChanged();
                }
                if (listener != null) {
                    listener.onSwipeLeft(place);
                }
            }

            @Override
            public void onSwipeRight() {
                if (places.contains(place)) {
                    places.remove(place);
                    notifyDataSetChanged();
                }
                if (listener != null) {
                    listener.onSwipeRight(place);
                }
            }
        }));

        convertView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        return convertView;
    }
}
