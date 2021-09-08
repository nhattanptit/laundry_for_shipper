package com.laundry.app.data;

import com.laundry.app.dto.addressall.AddressListResponse;
import com.laundry.app.dto.addressnew.AddressAddRequest;
import com.laundry.app.dto.addressnew.AddressAddResponse;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.order.OrderConfirmResponseDto;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponseDto;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.dto.shippingfee.ShippingFeeResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST(APIConstant.URL_SIGNUP)
    Call<RegisterResponse> signup(@Body RegisterRequest body);

    @POST(APIConstant.URL_LOGIN)
    Call<LoginResponseDto> signin(@Body LoginRequest loginRequest);

    @GET(APIConstant.URL_SERVICES_ALL)
    Call<ServiceListResponse> getServicesAll();

    @POST(APIConstant.URL_SERVICES_DETAILS)
    @FormUrlEncoded
    Call<ServicesDetailResponse> getServicesDetail(@Field("serviceId") int id);

    @POST(APIConstant.URL_ORDERS_CONFIRM)
    Call<OrderConfirmResponseDto> orderConfirm(@Header("Authorization") String token, @Body List<OrderServiceDetailForm> body);

    @POST(APIConstant.URL_ORDERS_CREATE)
    Call<OrderResponseDto> createOrder(@Header("Authorization") String token, @Body OrderRequest body);

    @GET(APIConstant.URL_DIRECTION_API)
    Call<MapDirectionResponse> getDirectionMap(@Path("coordinate") String coordinate, @Query("geometries") String geometries, @Query("access_token") String accessToken);

    @GET(APIConstant.URL_ADDRESS_ALL)
    Call<AddressListResponse> getAddress(@Header("Authorization") String token);

    @POST(APIConstant.URL_ADDRESS_NEW)
    Call<AddressAddResponse> addAddress(@Header("Authorization") String token, @Body AddressAddRequest body);

    @POST(APIConstant.URL_ORDER_SHIPPING_FEE)
    @FormUrlEncoded
    Call<ShippingFeeResponseDto> getShippingFee(@Header("Authorization") String token, @Field("distance") String distance);
}
