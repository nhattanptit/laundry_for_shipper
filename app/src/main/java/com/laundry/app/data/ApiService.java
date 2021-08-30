package com.laundry.app.data;

import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.test.NoteRequest;
import com.laundry.app.dto.test.NoteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/user/auth/signup")
    Call<RegisterResponse> saveInfoRegister(@Body RegisterRequest body);

    @POST("/note/create")
    Call<NoteResponse> test(@Body NoteRequest body);
}
