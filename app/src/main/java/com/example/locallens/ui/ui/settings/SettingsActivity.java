package com.example.locallens.ui.ui.settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locallens.BuildConfig;
import com.example.locallens.R;
import com.example.locallens.domain.repository.PlaceRepository;
import com.example.locallens.utils.PreferenceManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    PlaceRepository placeRepository;

    private Switch switchDarkMode;
    private Button btnClearIgnored;
    private TextView tvAppVersion;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnClearIgnored = findViewById(R.id.btnClearIgnored);
        tvAppVersion = findViewById(R.id.tvAppVersion);

        // Load dark mode preference
        switchDarkMode.setChecked(preferenceManager.isDarkModeEnabled());

        // Handle Switch toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDarkModeEnabled(isChecked);
            // Instruct user to restart activity/app for theme application if necessary
        });

        // Handle clear ignored places button click
        btnClearIgnored.setOnClickListener(v -> {
            // Call repository method to delete ignored places
            // Note: If clearIgnoredPlaces() is not yet in PlaceRepository, it needs to be added
            disposables.add(
                    placeRepository.clearIgnoredPlaces()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> Toast.makeText(this, "Ignored places cleared", Toast.LENGTH_SHORT).show(),
                                    throwable -> Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                            )
            );
        });

        // Display App version
        tvAppVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
