package com.example.locallens.ui.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.usecase.FetchNearbyPlacesUseCase;
import com.example.locallens.domain.usecase.GetNearbyPlacesUseCase;
import com.example.locallens.ui.state.UiState;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final GetNearbyPlacesUseCase getNearbyPlacesUseCase;
    private final FetchNearbyPlacesUseCase fetchNearbyPlacesUseCase;

    private final MutableLiveData<UiState<List<Place>>> _uiState = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public HomeViewModel(
            GetNearbyPlacesUseCase getNearbyPlacesUseCase,
            FetchNearbyPlacesUseCase fetchNearbyPlacesUseCase) {
        this.getNearbyPlacesUseCase = getNearbyPlacesUseCase;
        this.fetchNearbyPlacesUseCase = fetchNearbyPlacesUseCase;
    }

    public LiveData<UiState<List<Place>>> getUiState() {
        return _uiState;
    }

    public void loadNearbyPlaces() {
        _uiState.setValue(UiState.loading());

        disposables.add(
                fetchNearbyPlacesUseCase.execute()
                        .andThen(getNearbyPlacesUseCase.execute())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                places -> {
                                    if (places == null || places.isEmpty()) {
                                        _uiState.setValue(UiState.empty());
                                    } else {
                                        _uiState.setValue(UiState.success(places));
                                    }
                                },
                                throwable -> {
                                    _uiState.setValue(UiState.error(throwable.getMessage()));
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
