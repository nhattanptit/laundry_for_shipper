package com.laundry.app.control;

import android.content.Context;

import androidx.annotation.IntRange;

import com.laundry.app.data.APIConstant;
import com.laundry.app.data.ApiService;
import com.laundry.app.dto.UserInfo;
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

public class DataController {

    private final ApiService service = APIConstant.getService(APIConstant.BASE_URL);
    private final ApiService serviceMap = APIConstant.getService(APIConstant.BASE_URL_MAP_BOX);

    public void register(RegisterRequest registerRequest,
                         ApiServiceOperator.OnResponseListener<RegisterResponse> listener) {

        Call<RegisterResponse> call = service.signup(registerRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void login(LoginRequest loginRequest,
                      ApiServiceOperator.OnResponseListener<LoginResponseDto> listener) {
        Call<LoginResponseDto> call = service.signin(loginRequest);
        call.enqueue(new ApiServiceOperator<>(listener));

    }

    public void getServicesAll(ApiServiceOperator.OnResponseListener<ServiceListResponse> listener) {
        Call<ServiceListResponse> call = service.getServicesAll();
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getServicesDetail(@IntRange(from = 16, to = 19) int id, ApiServiceOperator.OnResponseListener<ServicesDetailResponse> listener) {
        Call<ServicesDetailResponse> call = service.getServicesDetail(id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void createOrder(Context context,
                            OrderRequest request,
                            ApiServiceOperator.OnResponseListener<OrderResponseDto> listener) {
        Call<OrderResponseDto> call = service.createOrder(UserInfo.getInstance().getToken(context), request);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    /**
     * Get direction map
     *
     * @param listener
     */
    public void getDirectionMaps(String coordinate,
                                 String geometries,
                                 String accessToken,
                                 ApiServiceOperator.OnResponseListener<MapDirectionResponse> listener) {
        Call<MapDirectionResponse> call = serviceMap.getDirectionMap(coordinate, geometries, accessToken);
        call.enqueue(new ApiServiceOperator<MapDirectionResponse>(listener));
    }

    public void getAddress(Context context, ApiServiceOperator.OnResponseListener<AddressListResponse> listener) {
        Call<AddressListResponse> call = service.getAddress(UserInfo.getInstance().getToken(context));
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void oderConfirm(Context context,
                            List<OrderServiceDetailForm> orderServiceDetailForms,
                            ApiServiceOperator.OnResponseListener<OrderConfirmResponseDto> listener) {
        Call<OrderConfirmResponseDto> call = service.orderConfirm(UserInfo.getInstance().getToken(context), orderServiceDetailForms);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void getShippingFee(Context context,
                               String distance,
                               ApiServiceOperator.OnResponseListener<ShippingFeeResponseDto> listener) {
        Call<ShippingFeeResponseDto> call = service.getShippingFee(UserInfo.getInstance().getToken(context), distance);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void addAddress(Context context, AddressAddRequest addRequest,
                           ApiServiceOperator.OnResponseListener<AddressAddResponse> listener) {
        Call<AddressAddResponse> call = service.addAddress(UserInfo.getInstance().getToken(context), addRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void deleteAddress(Context context, int id, ApiServiceOperator.OnResponseListener<AddressDeleteResponse> listener) {
        Call<AddressDeleteResponse> call = service.deleteAddress(UserInfo.getInstance().getToken(context), id);
        call.enqueue(new ApiServiceOperator<>(listener));
    }

    public void updateAddress(Context context, int id, AddressUpdateRequest updateAddressRequest, ApiServiceOperator.OnResponseListener<AddressUpdateResponse> listener) {
        Call<AddressUpdateResponse> call = service.updateAddress(UserInfo.getInstance().getToken(context), id, updateAddressRequest);
        call.enqueue(new ApiServiceOperator<>(listener));
    }
}
