package com.laundry.app.data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/get/abc")
    Call<Boolean> getData();
}
