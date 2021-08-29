package com.laundry.app.data;

import com.laundry.app.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private Retrofit mRetrofit;

    public Retrofit getClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.level(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
