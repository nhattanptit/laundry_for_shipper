package com.laundry.app.data;

import com.laundry.app.dto.BaseResponse;
import com.laundry.app.dto.addressall.AddressListResponse;
import com.laundry.app.dto.addressdelete.AddressDeleteResponse;
import com.laundry.app.dto.addressnew.AddressAddRequest;
import com.laundry.app.dto.addressnew.AddressAddResponse;
import com.laundry.app.dto.addressupdate.AddressUpdateRequest;
import com.laundry.app.dto.addressupdate.AddressUpdateResponse;
import com.laundry.app.dto.authentication.LoginRequest;
import com.laundry.app.dto.authentication.LoginResponseDto;
import com.laundry.app.dto.authentication.RegisterRequest;
import com.laundry.app.dto.authentication.RegisterResponse;
import com.laundry.app.dto.authentication.SocialLoginRequest;
import com.laundry.app.dto.authentication.SocialLoginRequestLite;
import com.laundry.app.dto.maps.MapDirectionResponse;
import com.laundry.app.dto.order.OrderConfirmResponseDto;
import com.laundry.app.dto.ordercreate.OrderRequest;
import com.laundry.app.dto.ordercreate.OrderResponseDto;
import com.laundry.app.dto.ordercreate.OrderServiceDetailForm;
import com.laundry.app.dto.orderincompletelist.OrderListIncompleteCustomerResponse;
import com.laundry.app.dto.orderlistcustomer.OrderListCustomerResponse;
import com.laundry.app.dto.orderlistshipper.OrderListShipperResponse;
import com.laundry.app.dto.payment.PaymentRequest;
import com.laundry.app.dto.payment.PaymentResponseDto;
import com.laundry.app.dto.servicelist.ServiceListResponse;
import com.laundry.app.dto.sevicedetail.ServicesDetailResponse;
import com.laundry.app.dto.shippingfee.ShippingFeeResponseDto;
import com.laundry.app.dto.user.PersonalInfoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST(APIConstant.URL_SIGNUP)
    Call<RegisterResponse> signup(@Body RegisterRequest body);

    @POST(APIConstant.URL_LOGIN)
    Call<LoginResponseDto> signin(@Body LoginRequest loginRequest);

    @POST(APIConstant.URL_SOCIAL_LOGIN_FIRST_TIME)
    Call<LoginResponseDto> signinSocialFirstTime(@Body SocialLoginRequest body);

    @POST(APIConstant.URL_SOCIAL_LOGIN_SECOND_TIME)
    Call<LoginResponseDto> signinSocialSecordTime(@Body SocialLoginRequestLite body);

    @POST(APIConstant.URL_SHIPPER_LOGIN)
    Call<LoginResponseDto> signinShipper(@Body LoginRequest loginRequest);

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

    @DELETE(APIConstant.URL_ADDRESS_DELETE)
    Call<AddressDeleteResponse> deleteAddress(@Header("Authorization") String token, @Query("addressId") int id);

    @PUT(APIConstant.URL_ADDRESS_UPDATE)
    Call<AddressUpdateResponse> updateAddress(@Header("Authorization") String token, @Query("addressId") int id, @Body AddressUpdateRequest addressUpdateRequest);

    @POST(APIConstant.URL_ORDER_SHIPPING_FEE)
    @FormUrlEncoded
    Call<ShippingFeeResponseDto> getShippingFee(@Header("Authorization") String token, @Field("distance") String distance);

    @PUT(APIConstant.URL_ORDERS_CANCEL)
    Call<BaseResponse> cancelOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @GET(APIConstant.URL_ORDER_LIST_CUSTOMER)
    Call<OrderListCustomerResponse> getOrderListCustomer(@Header("Authorization") String token, @Query("page") int page, @Query("size") int size);

    @GET(APIConstant.URL_ORDER_INCOMPLETE_LIST_CUSTOMER)
    Call<OrderListIncompleteCustomerResponse> getOrderIncompleteListCustomer(@Header("Authorization") String token, @Query("page") int page, @Query("size") int size);

    @POST(APIConstant.URL_ORDERS_DETAILS)
    @FormUrlEncoded
    Call<OrderResponseDto> getOrderDetail(@Header("Authorization") String token, @Field("orderId") int id);

    @POST(APIConstant.URL_ORDERS_DETAILS_SHIPPER)
    @FormUrlEncoded
    Call<OrderResponseDto> getOrderDetailShipper(@Header("Authorization") String token, @Field("orderId") int id);

    @GET(APIConstant.URL_ORDER_LIST_SHIPPER)
    Call<OrderListShipperResponse> getOrderListShipper(@Header("Authorization") String token, @Query("orderStatus")String orderStatus , @Query("page") int page, @Query("size") int size);

    @GET(APIConstant.URL_ORDER_LIST_NEW_SHIPPER)
    Call<OrderListShipperResponse> getOrderListNewShipper(@Header("Authorization") String token , @Query("page") int page, @Query("size") int size);

    @PUT(APIConstant.URL_ORDER_ACCEPT_ORDER)
    Call<BaseResponse> acceptOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @PUT(APIConstant.URL_ORDERS_PAYMENT)
    Call<PaymentResponseDto> paymentFinished(@Header("Authorization") String token, @Body PaymentRequest request);

    @PUT(APIConstant.URL_ORDER_RECEIVE_ORDER)
    Call<BaseResponse> receiveOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @PUT(APIConstant.URL_ORDER_DELIVER_ORDER)
    Call<BaseResponse> deliverOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @PUT(APIConstant.URL_ORDER_COMPLETE_ORDER)
    Call<BaseResponse> completeOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @PUT(APIConstant.URL_ORDER_SHIPPER_CANCEL)
    Call<BaseResponse> shipperCancelOrder(@Header("Authorization") String token, @Query("orderId") String orderId);

    @POST(APIConstant.URL_USER_INFO)
    Call<PersonalInfoResponse> getAccountInfomation(@Header("Authorization") String token);
}
