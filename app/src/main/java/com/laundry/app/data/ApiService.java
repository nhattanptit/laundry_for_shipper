package com.laundry.app.data;

import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.ordercreate.OrderDto;
import com.laundry.app.dto.ordercreate.OrderResponse;
import com.laundry.app.dto.serviceall.ServiceAllResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST(APIConstant.URL_SIGNUP)
    Call<RegisterResponse> signup(@Body RegisterRequest body);

    @POST(APIConstant.URL_LOGIN)
    Call<LoginResponseDto> signin(@Body LoginRequest loginRequest);

    @GET(APIConstant.URL_SERVICES_ALL)
    Call<ServiceAllResponse> getServicesAll();

    @POST(APIConstant.URL_SERVICES_DETAILS)
    @FormUrlEncoded
    Call<ServicesDetailResponse> getServicesDetail(@Field("serviceId") int id);

    @POST(APIConstant.URL_ORDERS_CREATE)
    Call<OrderResponse> createOrder(@Body OrderDto body);

}
