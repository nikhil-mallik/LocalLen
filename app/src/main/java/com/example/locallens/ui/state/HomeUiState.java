package com.example.locallens.ui.state;

import com.example.locallens.domain.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the UI state for the Home screen.
 */
public class HomeUiState {

    private boolean isLoading;
    private List<Place> places;
    private String errorMessage;

    public HomeUiState() {
        this.isLoading = false;
        this.places = new ArrayList<>();
        this.errorMessage = null;
    }

    public HomeUiState(boolean isLoading, List<Place> places, String errorMessage) {
        this.isLoading = isLoading;
        this.places = places != null ? places : new ArrayList<>();
        this.errorMessage = errorMessage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
