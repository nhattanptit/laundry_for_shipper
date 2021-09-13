package com.laundry.app.control;

import android.util.Log;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiServiceOperator<T> implements Callback<T> {

    public interface OnResponseListener<T> {
        void onSuccess(T body);

        void onFailure(Throwable t);
    }

    private final OnResponseListener<T> onResponseListener;

    public ApiServiceOperator(OnResponseListener<T> onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            onResponseListener.onSuccess(response.body());
        } else {
            onResponseListener.onFailure(new ServerErrorException());
        }
        Log.d("TAG", "onResponse: "+ response.toString());
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onResponseListener.onFailure(new ConnectionErrorException());
    }

    // these exception can be on a separate classes.
    public static class ServerErrorException extends Exception {
    }

    public static class ConnectionErrorException extends Exception {
    }
}