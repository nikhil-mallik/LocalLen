package com.example.locallens.ui.state;

public class UiState<T> {

    private final boolean isLoading;
    private final T data;
    private final String errorMessage;
    private final boolean isEmpty;

    private UiState(boolean isLoading, T data, String errorMessage, boolean isEmpty) {
        this.isLoading = isLoading;
        this.data = data;
        this.errorMessage = errorMessage;
        this.isEmpty = isEmpty;
    }

    public static <T> UiState<T> loading() {
        return new UiState<>(true, null, null, false);
    }

    public static <T> UiState<T> success(T data) {
        return new UiState<>(false, data, null, false);
    }

    public static <T> UiState<T> error(String errorMessage) {
        return new UiState<>(false, null, errorMessage, false);
    }

    public static <T> UiState<T> empty() {
        return new UiState<>(false, null, null, true);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
