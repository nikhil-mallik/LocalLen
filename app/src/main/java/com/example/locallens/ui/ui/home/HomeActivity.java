package com.example.locallens.ui.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.locallens.R;
import com.example.locallens.databinding.ActivityHomeBinding;
import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.usecase.AddToFavoritesUseCase;
import com.example.locallens.domain.usecase.IgnorePlaceUseCase;
import com.example.locallens.ui.state.UiState;
import com.example.locallens.ui.ui.auth.LoginActivity;
import com.example.locallens.ui.ui.favorites.FavoritesActivity;
import com.example.locallens.ui.ui.profile.ProfileActivity;
import com.example.locallens.ui.ui.settings.SettingsActivity;
import com.example.locallens.utils.PermissionHelper;
import com.example.locallens.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.view.Menu;
import android.view.MenuItem;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;

    @Inject
    SessionManager sessionManager;

    @Inject
    AddToFavoritesUseCase addToFavoritesUseCase;

    @Inject
    IgnorePlaceUseCase ignorePlaceUseCase;

    @Inject
    FirebaseAuth firebaseAuth;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize ViewBinding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        if (!PermissionHelper.hasLocationPermission(this)) {
            PermissionHelper.requestLocationPermission(this);
        } else {
            viewModel.loadNearbyPlaces();
        }

        // Setup Observers
        observeUiState();

        // Retry button click listener
        binding.btnRetry.setOnClickListener(v -> viewModel.loadNearbyPlaces());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuFavorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        } else if (itemId == R.id.menuProfile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (itemId == R.id.menuSettings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (itemId == R.id.menuLogout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        firebaseAuth.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == PermissionHelper.LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                viewModel.loadNearbyPlaces();

            } else {

                Toast.makeText(
                        this,
                        "Location permission is required",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private void observeUiState() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) {
                return;
            }

            if (state.isLoading()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.cardContainer.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.GONE);
                binding.btnRetry.setVisibility(View.GONE);
                // Also hide legacy tvEmpty if still present playfully
                if (binding.tvEmpty != null) binding.tvEmpty.setVisibility(View.GONE);
            } else if (state.isEmpty()) {
                binding.progressBar.setVisibility(View.GONE);
                binding.cardContainer.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText("No places available");
                binding.btnRetry.setVisibility(View.GONE);
                if (binding.tvEmpty != null) binding.tvEmpty.setVisibility(View.GONE);
            } else if (state.getErrorMessage() != null) {
                binding.progressBar.setVisibility(View.GONE);
                binding.cardContainer.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText(state.getErrorMessage());
                binding.btnRetry.setVisibility(View.VISIBLE);
                if (binding.tvEmpty != null) binding.tvEmpty.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.GONE);
                binding.btnRetry.setVisibility(View.GONE);
                if (binding.tvEmpty != null) binding.tvEmpty.setVisibility(View.GONE);
                binding.cardContainer.setVisibility(View.VISIBLE);
                
                displayCards(state.getData());
            }
        });
    }

    private void displayCards(List<Place> places) {
        binding.cardContainer.removeAllViews();

        SwipeCardAdapter adapter = new SwipeCardAdapter(this, places, new SwipeCardAdapter.CardSwipeListener() {
            @Override
            public void onSwipeLeft(Place place) {
                if (!sessionManager.isUserLoggedIn()) {
                    Toast.makeText(HomeActivity.this, "Login required to add favorites", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    return;
                }

                disposables.add(
                        addToFavoritesUseCase.execute(place)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> Toast.makeText(HomeActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show(),
                                        e -> Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                )
                );
            }

            @Override
            public void onSwipeRight(Place place) {
                disposables.add(
                        ignorePlaceUseCase.execute(place.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> Toast.makeText(HomeActivity.this, "Place ignored", Toast.LENGTH_SHORT).show(),
                                        e -> Toast.makeText(HomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                )
                );
            }
        });

        // Render cards securely inside the container. 
        // Adding them in reverse order ensures the 0th item is at the very top of the Z-index stack.
        for (int i = adapter.getCount() - 1; i >= 0; i--) {
            View cardView = adapter.getView(i, null, binding.cardContainer);
            binding.cardContainer.addView(cardView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
