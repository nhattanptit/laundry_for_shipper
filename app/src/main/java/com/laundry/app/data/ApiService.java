package com.laundry.app.data;

import com.laundry.app.dto.serviceall.ServiceAllBody;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST(APIConstant.URL_SIGNUP)
    Call<RegisterResponse> signup(@Body RegisterRequest body);

    @GET(APIConstant.URL_SERVICES_ALL)
    Call<ServiceAllBody> getServicesAll();

}
