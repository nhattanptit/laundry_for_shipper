package com.laundry.app.data;

import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST(APIConstant.URL_SIGNUP)
    Call<RegisterResponse> signup(@Body RegisterRequest body);

    @POST(APIConstant.URL_LOGIN)
    Call<LoginResponseDto> signin(@Body LoginRequest loginRequest);
}
