package com.example.locallens.ui.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.usecase.GetFavoritePlacesUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FavoritesViewModel extends ViewModel {

    private final GetFavoritePlacesUseCase getFavoritePlacesUseCase;
    private final MutableLiveData<List<Place>> favorites = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public FavoritesViewModel(GetFavoritePlacesUseCase getFavoritePlacesUseCase) {
        this.getFavoritePlacesUseCase = getFavoritePlacesUseCase;
    }

    public LiveData<List<Place>> getFavorites() {
        return favorites;
    }

    public void loadFavorites() {
        disposables.add(
                getFavoritePlacesUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                places -> favorites.setValue(places),
                                throwable -> {
                                    // Handle error if necessary
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
